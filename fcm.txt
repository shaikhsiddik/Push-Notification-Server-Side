var express = require('express')
var bodyParser = require('body-parser')
var app = express()
var http = require('http').Server(app)
var https = require('https')
var port = process.env.PORT || 3000
const serverKey = 'key=<Your server legacy key here>'

app.use(bodyParser.urlencoded({
    extended: true
}))

app.use(bodyParser.json())

http.listen(port, function () {
    console.log('server listening on port ' + port)
})

app.get('/', (req, res) => {
    res.send('Hello!')
}
)

app.post('/send_push', (req, res) => {

    console.log(req.body);

    /**
     * Message json object
     * Refer below link for add params in message data json object
     * https://firebase.google.com/docs/cloud-messaging/http-server-ref
     */
    var message = {
        registration_ids: req.body.registration_ids,
        android_channel_id: req.body.android_channel_id,

        data: {
            sender_uid: req.body.sender_uid,
            sender_name: req.body.sender_name,
            message: req.body.message
        }
    }

    const data = JSON.stringify(message)

    const options = {
        hostname: 'fcm.googleapis.com',
        path: '/fcm/send',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": serverKey,
            'Content-Length': data.length
        }
    }

    /**
     * Rest api function for send fcm push notification
     */
    const request = https.request(options, (fcmres) => {
        // console.log(fcmres)
        // console.log('ErrorCode ' + fcmres.statusCode)
        // console.log('ErrorMessage ' + fcmres.statusMessage)

        if (fcmres.statusCode == 200) {
            fcmres.on('data', (d) => {
                try {
                    res.send(JSON.parse(d))
                } catch (e) {
                    errString = {
                        statusCode: '408',
                        statusMessage: 'Error in response'
                    }
                    res.send(errString)
                }
            })
        } else {
            errString = {
                statusCode: fcmres.statusCode,
                statusMessage: fcmres.statusMessage
            }
            res.send(errString)
        }
    })

    /**
     * This invoke when faild to call api
     */
    request.on('error', (error) => {
        console.error(error)
        errString = {
            statusCode: '408',
            statusMessage: 'Error to connect server'
        }
        res.send(errString)
    })

    request.write(data)
    request.end()
})