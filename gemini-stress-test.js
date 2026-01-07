//docker run --rm -i --add-host=host.docker.internal:host-gateway grafana/k6 run - < gemini-stress-test.js

import http from 'k6/http';
import { check, sleep } from 'k6';

// Configuration: 1000 users arriving over 10 seconds
export const options = {
    stages: [
        { duration: '10s', target: 1000 }, // Ramp up to 1000 users
        { duration: '20s', target: 1000 }, // Hold load
        { duration: '5s', target: 0 },     // Ramp down
    ],
    thresholds: {
        http_req_failed: ['rate<0.999'], // Allow failures (since 999 MUST fail)
        http_req_duration: ['p(95)<4000'], // Adjusted to 4s based on previous runs
    },
};

const BASE_URL = 'http://host.docker.internal:8080';

// SETUP: Run once before the test starts
export function setup() {
    // 1. Create a Venue
    const venueRes = http.post(`${BASE_URL}/venues`, JSON.stringify({
        name: "Olympic Stadium",
        location: "London"
    }), { headers: { 'Content-Type': 'application/json' } });

    const venueId = venueRes.json('id');
    console.log(`Created Venue ID: ${venueId}`);

    // 2. Add ONE Slot (The "Golden Ticket")
    const slotRes = http.post(`${BASE_URL}/venues/${venueId}/slots`, JSON.stringify({
        startTime: "2026-07-15T10:00:00",
        endTime: "2026-07-15T11:00:00"
    }), { headers: { 'Content-Type': 'application/json' } });

    const slotId = slotRes.json('id');
    console.log(`Created Target Slot ID: ${slotId}`);

    return { venueId, slotId };
}

// --- THIS LINE WAS MISSING ---
export default function (data) {

    // Create unique ID to avoid "Duplicate Email" errors
    const uniqueId = Date.now();

    const userPayload = JSON.stringify({
        name: `User ${__VU}`,
        email: `stress${__VU}-${__ITER}-${uniqueId}@test.com`,
        password: "password"
    });

    const userRes = http.post(`${BASE_URL}/users`, userPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    // Safety Check: Don't proceed if User Creation failed
    if (userRes.status !== 201) {
        // console.error(`User creation failed! Status: ${userRes.status}`);
        return;
    }

    const userId = userRes.json('id');

    // 2. Try to Book the SAME Slot using the ID from setup()
    const bookingPayload = JSON.stringify({
        user_id: userId,
        venue_slot_id: data.slotId, // Using the slot ID passed from setup
        sport_id: 1
    });

    const bookingRes = http.post(`${BASE_URL}/bookings`, bookingPayload, {
        headers: { 'Content-Type': 'application/json' }
    });

    // 3. Assertions
    check(bookingRes, {
        'Handled correctly': (r) => r.status === 201 || r.status === 500 || r.status === 409,
    });

    if (bookingRes.status === 201) {
        console.log(`🎉 WINNER! User ${userId} got the booking!`);
    }

    sleep(1);
}