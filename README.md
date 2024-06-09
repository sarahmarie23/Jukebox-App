# Jukebox Player App

Sarah Martel  
Version 1 

6/8/24

## Summary of Project

This app serves as a companion app to the jukebox receiver device, which I will contribute to start its design and production in the coming months. Owners of the device will be able to plug it into their jukebox and control their jukebox from the Jukebox Player app. They can play all the songs in their jukebox and use their phone like a remote to queue up the music from their vinyls or CDs. Much like the classic way of making selections on the machine while standing at the jukebox, this can now be done from sitting at a distance, easily on your phone. 

## Project Analysis

### Value Proposition

Jukebox owners have expressed interest in such a way to control their jukebox from their phone. While you can purchase a jukebox that you can control with an app, collectors would like to be able to play their own unique collectible machines. Customers would like to be able to control the music being played without having to stand right in front of the jukebox. They have most notably requested the ability to be able to take a call on their phone, and the jukebox stops playing music, without them having to go to it and stop it manually, or walk into another room to hear the call. Version 1.0 of our app provides users the ability to make song selections, while the next release will enable phone call listening and music control features. Users can now be the ultimate at-home Jukebox DJ, all remotely from their phone.

### Primary Purpose

The purpose is to enhance the experience of owning a collectible jukebox and bring a solution to a problem that customers have brought up.

### Target Audience

Vintage jukebox collectors, predominantly men aged 60+ in the US and Europe. The family-owned business producing the receiver will start out by making it compatible with the Wurlitzer One More Time 1015 model (vinyl and CD versions) since that is the most popular vintage jukebox model. After the MVP stage, receivers that are compatible with other models will be created. The app will be compatible with users of multiple devices. The receiver will be sold primarily on eBay, and also on Facebook Marketplace, with instructions to download the free app from the App/Play Store.

<div style="text-align:center">

![Bubbler jukebox](https://www.newretrodining.com/images/one_more_time_jukebox.jpg)

*This is the Wurlitzer One More Time 1015 jukebox. Vinyl version started selling in 1986 and the CD version in 1990.* [https://jukeboxes-uk.com/history-of-wurlitzer/](https://jukeboxes-uk.com/history-of-wurlitzer/)
</div>

### Success Criteria

If the customers leave satisfied reviews on eBay and don’t leave complaints, and we sell enough to turn a profit, even if that is in the future after compatibility for other jukeboxes are added, then we will be successful.

### Competitor Analysis

*TouchTunes* - This is an app used to control a jukebox at a bar, by buying credits. It is easy to use and fun, but only works on the modern jukeboxes you’d see in a bar.

*AMI Music* -  Similar to TouchTunes, it works for customers to play music on a jukebox at a bar. It does have higher ratings on both the App and Play Store. Interestingly, AMI is one of the companies that had a large influence on the golden age of jukeboxes during the early-mid 1900s and beyond. Once again, their app will only work on their jukeboxes and not for home use.


<img src="https://www.fiftiesstore.com/media/opti_image/webp/catalog/product/cache/8b6431de9410c89d24dfb37cd261db29/o/m/omtk99-remote_xxl2.webp" alt="Remote and receiver for jukebox" width="300">

[https://www.fiftiesstore.com/wurlitzer-one-more-time-omt-vinyl-and-cd-k99-remote-control.html](https://www.fiftiesstore.com/wurlitzer-one-more-time-omt-vinyl-and-cd-k99-remote-control.html)

While not an app, it is worth mentioning this as a competitor because this is a device that you connect to the inside of the jukebox and use the remote to select the songs. Our app and device will have the functionality of this remote and device, with the added value of it being bluetooth and will automatically pause when the user receives a phone call. It is selling for 550 euros (about $586).

### Monetization Model

The app will be free, because it will only work with our receiver, which could sell for $500.

## Initial Design

### UI/UX Design

The first step for the app will be helping the user to connect to their jukebox via bluetooth. The app first asks the user to allow the permissions necessary to run the app, and will not continue unless the user agrees. The Bluetooth pairing dialog box will pop up when the user selects the receiver from the visible devices list. It prompts the user to connect and turn on the receiver and jukebox if it is not detected by the phone. Once a connection is established, it will ask the user to verify the type of jukebox (vinyl or CD). There will be simple, large buttons to play a song, stop, pause (CD only). If the user taps the play button, the number keypad will pop up asking for the disc number and the song/side number. The number system is slightly different for vinyl or CD but for all versions, a number signal is being sent out corresponding to the disc.
In a future version, if the user receives a call, the jukebox will stop playing. When the user hangs up the call, the music will start playing again.

### Wireframe Drawings

This is my initial concept drawings for how the app might look like.

![Wireframe 1](https://raw.githubusercontent.com/sarahmarie23/Jukebox-App/master/wireframe1.jpg)

![Wireframe 2](https://raw.githubusercontent.com/sarahmarie23/Jukebox-App/master/wireframe2.jpg)

### Technical Architecture

* The ability to make a Bluetooth connection.
* * Storing information about the jukebox: type (vinyl or CD), name, model (for now we only have one model supported), connection status, last played song
* *A mapping of the jukebox buttons to the song titles. For CDs, this will be a 2 digit CD and 2 digit track number. Vinyls will just use a 2 digit value.* A number keypad to make song selections. Future version will validate for CD and vinyl selections.
* ~~Discogs API for gathering song metadata~~

## Challenges and Open Questions

**Getting Bluetooth working** - It seems a bit complicated and might take some time trying to get it to work. Ideally I’d get it to connect to an Arduino, since the actual final product would have one inside it. As an MVP demonstration, I could have it transmit Bluetooth data to my computer, and the data could be printed out in a terminal. It was a huge struggle to get Bluetooth working. It still has some complications, but I have proven that the app can transmit to and receive data from a microcontroller. More testing will be done on alternative components and microcontrollers to find the most appropriate device for the actual receiver. 

**Testing** - I won’t have a real jukebox or an actual receiver device to test the app with. As a solution, I will be simulating the app’s functionality by sending Bluetooth signals to an Arduino that will then light in a certain sequence or display something on a screen. The finished app and device will work by transmitting a number that corresponds to the disc/song and the receiver will translate that number signal into the correct disc. 

**Getting data** - This is something that will be two versions from now: *I need to do more research on how the song information will be put into the app. There’s the Spotify API, which has access to metadata, but it has a cost after 500 requests per month. And it wouldn’t have information about records. There is the Discogs database, which includes information on vinyl records and has an API to use. I could use a website like [https://45cat.com](https://45cat.com) and get the data straight from the website.*

**Using an API** - I’ve never used an API, will I be able to learn in time? Could be a knowledge limitation. To overcome this, I will start working on this early and do my best to not procrastinate.

**Question 1:** How would users prefer to make song selections? I was told to make it like a remote with number buttons. Would customers prefer that (the MVP version) or would they want something more visual like scrolling through a playlist in a music player app (Spotify)?

**Question 2:** How will the users create a playlist? The brute force method would be manually typing in each song name into a list mapped to the button number that corresponds to it in the jukebox. Ideally, they would search for the song name and then some data would be downloaded to create the playlist (album cover, artist, date). I need to get some opinions from others on what they would prefer.

## Reflection on the initial app version (as what was submitted 6/5/24)

### Accomplishing the stated objective 

My ultimate goal is to have an app that will be able to communicate with a receiver that is plugged inside a jukebox. The app will control the music being played from the jukebox. Since I don't have an actual jukebox or experience (yet) in electronics/hardware, my objective for this initial version was to have the app work with an Arduino. I stayed with my happy/golden path, which was to insist on the necessary permissions, walk the user to connecting to the receiver, receive data about the machine type, and send the song number. Also the state should be maintained. Which I did.

### Delivering on the value proposition

The value of the app is that you can control your jukebox from the comfort of at a distance from the jukebox. It does the minimum controls and at a distance because it uses Bluetooth.

### User privacy and security

Like most apps, this one does not have a username and password login, so there's no need to store sensitive user data. The information that is stored is stored directly in the phone, 

# Mockup for the app

4/17/24

I have included here a page of screens that pertain to the jukebox app (actual name TBD) [Click here to view the screens.](https://raw.githubusercontent.com/sarahmarie23/Jukebox-App/master/AppMockupScreens.jpg)

I also have a flowchart to show the "golden path" of the app, [which you can check out here.](https://raw.githubusercontent.com/sarahmarie23/Jukebox-App/master/SarahMartelAppMockup.jpg)

This design is relevant to the target audience, which is jukebox collectors, because it works with the specified type of jukebox (CD or vinyl) that they own. It also gives them the value of seamless transitions to receiving a phone call while their music is playing, and resuming it afterwards, as requested by customers. It is simple and easy to use.

# Jukebox App Prototype

4/28/24

While it would be nice to have, it is unnecessary to have a playlist building functionality. I would prefer to get everything solid before attempting a playlist builder.

## What's needed for the MVP

* **Bluetooth functionality** - currently, the app asks for permission from the user to use the necessary permissions. If the Bluetooth is off, it will prompt the user to turn it on. It's hard to test the Bluetooth functionality when I don't have the Arduino working yet.

* **Arduino setup** - The Arduino connects to my PC and I added a basic program to test if its working. You can see the Arduio on the list of Bluetooth devices on the phone, but only twice has it connected to the Arduino. I know this because the output in Arduino IDE displayed it in Serial Monitor.

* **Sending and receiving data** - Currently, if you enter a number on the remote, it *says* it was sent, but nothing actually happens right now. I would like it so that it sends and the Arduino either lights up or an output appears in a terminal.

* **Validating user input** - While not *technically* required, I'd be disappointed in myself if I didn't try to take care of this. Things like making sure valid numbers are entered, limiting the length of the jukebox name so that it doesn't overflow on the container (or maybe accounting for that code).

* **Receiving a call** - I haven't touched this, but the app should allow the user to stop/pause the song if they are receiving a call.

## Features -> Values

* Remote screen to input song numbers -> Intuitive way to control a jukebox from a phone

* Transmits song selection via Bluetooth -> Can control the music being played without being right in front of the jukebox

* The ability to mute the jukebox when a call is incoming -> Better experience taking phone calls, won't have to go over to the jukebox to pause it

## Prototype in Action

https://github.com/sarahmarie23/Jukebox-App/assets/57870970/9aecc6c8-0fe2-4a7d-a5a0-21ff5dd9de91

# Checkpoint 3 - Basic Functionality

5/12/24

**Updates** - I don't really have anything I am updating or changing from the original design, but I am prioritizing the remaining pieces in this order. 

**1. Get Bluetooth communication properly working** - I have spent a lot of time debugging and making the app and Arduino inch closer together to communicating without issues, but haven't quite got there yet. First, I need the Arduino to send over some data (referring to the Jukebox type), and then the song data will be able to be sent from the phone. I have the code written out for sending the data, but they have to connect before I can even test it out.

**2. Work on the remote screen** - I want the UI to be different for vinyl vs CD machines. Vinyl machines have 100 song choices, while CD machines have up to 100 CDs and the track limit would just be however many tracks that CD has. So there should be 1 or 2 text boxes depending on the machine type.

**3. Listen for phone calls** - One of the values of the app was that you could pause the music if you had an incoming phone call. I would like to make that happen. To simulate pausing, it would send over a value in the same way that the song selections are sent over.

**4. Validating user input** - If a user enters a number outside of the range, it should show an error and prompt them to enter the correct value.

