const express = require('express');

const app = express();

app.get('/', (req, res) => {
    const color = process.env.APP_COLOR || 'black';
    res.send(`<span style="color: ${color};">Hi there</span>`);
});
app.listen(8080, () => {
    console.log('Listening on port 8080');
});
