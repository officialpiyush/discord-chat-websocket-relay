<div align="center">
    <img src="https://i.vgy.me/iriTkw.png" alt="logo" align="center">
    <br>
    <br>
    <strong><i>Relay Messages between Discord's chat to any place using websockets.</i></strong>
    <br>
        <br>
        <a href="https://discord.gg/hWbb4Ee">
            <img src="https://img.shields.io/discord/543812119397924886.svg?style=for-the-badge&colorB=7289DA" alt="Support">
        </a>
</div>

---

# What's This?

Just simple server which relays messages from a discord text channel to websockets.

# How It's Done?

### Websocket Messages

* All websocket messages are sent in a stringified JSON format which can be represented as following in Typescript:
    ```typescript
    interface Websocket {
  
        // Whether the message if from discord. (To avoid loopholes)
        isFromDiscord: boolean;
  
        // Message Sent.
        message: string;
  
        // The tag of discord's Author Object.
        tag: string;
        
        // The Avatar URL of the user (Discord)
        url: string;
  } 
  ``` 
  
  * Anyone wanting to use this should take a notice of this format and make the client accordingly.
  * Frontend for this project soon!
  
  # Author
  * [Piyush](https://github.com/officialpiyush)