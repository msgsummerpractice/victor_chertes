const express = require('express');
const redis = require('redis');
const os = require('os');

const app = express();

const client = redis.createClient({
    host: process.env.REDIS_HOST || 'redis-server',
    port: process.env.REDIS_PORT || 6379
});

// Initialize 'visits' to 0 only if it doesn't exist to allow persistence
client.setnx('visits', 0);

app.get('/', (req, res) => {
    const greeting = process.env.GREETING || 'Number of visits is ';
    client.get('visits', (err, visits) => {
        const currentVisits = visits === null ? 0 : visits;
        res.send(
            greeting + currentVisits +
            '<br>Hostname: ' + os.hostname()
        );
        client.set('visits', parseInt(currentVisits) + 1);
    });
});

app.listen(8081, () => {
    console.log('Listening on port 8081');
});