
# Gupshup

Android Mobile Application built with java on Android Studio. It is integrated with an AI Chatbot which uses Google Gemini 1.5 flash API model to answer user queries. 


## Demo

The following shows the demo of how the applications works


https://github.com/user-attachments/assets/f018e564-fbe0-4efa-93bf-a69da53fea48


## Documentation

Follow this 
[Documentation](https://ai.google.dev/gemini-api/docs)
to understand and implement the integration of Gemini model as your chatbot in the application

## API Keys

To run this project, you will need to add the following environment variables to your .env file

`GEMINI_KEY` for the Chatbot

`FCM_API_KEY` for adding real-time message notification using Google Firebase Cloud Messaging. Follow the [Documentation](https://firebase.google.com/docs/cloud-messaging/concept-options) to understand in-depth. This will also help you to implement.


## Libraries used
The following libraries must be added to the 
`
  build.gradle
` in the project

1. OkHttp for handling HTTP requests and responses

```bash
  dependencies {
       // define a BOM and its version
       implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

       // define any required OkHttp artifacts without version
       implementation("com.squareup.okhttp3:okhttp")
       implementation("com.squareup.okhttp3:logging-interceptor")
    }
```

2. Country Code Picker (CCP) is an android library which provides an easy way to search and select country or country phone code for the telephone number.

```bash
  dependencies {
          implementation 'com.hbb20:ccp:X.Y.Z'
        }
```
- Version 2.3.1 and above uses AndroidX
- Version 2.5.0 and above hosted on MavenCentral
