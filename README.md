# Jukebox Player App

Sarah Martel  
Version 1

## Summary of Project

This app serves as a companion app to the [TBD] jukebox receiver device. If a customer buys the device, they will be able to plug it into their jukebox and control their jukebox from the Jukebox Player app. They can create a playlist and play all the songs in their jukebox and use their phone like a remote to play, stop, skip, repeat, and resume playing the music from their vinyls or CDs.

## Project Analysis

### Value Proposition

Jukebox owners have expressed interest in such a way to control their jukebox from their phone. While you can purchase a jukebox that you can control with an app, collectors would like to be able to play their own unique collectible machines. Customers would like to be able to control the music being played without having to stand right in front of the jukebox. They have most notably requested the ability to be able to take a call on their phone, and the jukebox stops playing music, without them having to go to it and stop it manually, or walk into another room to hear the call. Our app will be able to do that, along with allowing the user to create their own playlists from the music in their machine, and controlling the music remotely from their phone.

### Primary Purpose

The purpose is to enhance the experience of owning a collectible jukebox and bring a solution to a problem that customers have brought up.

### Target Audience

Vintage jukebox collectors, predominantly men aged 60+ in the US and Europe. The company producing the receiver will be starting out by making it compatible with the Wurlitzer One More Time 1015 model (vinyl and CD versions) since that is the most popular model of vintage jukebox. After the MVP stage, receivers that are compatible with other models will be created. The app will be compatible with users of multiple devices. The receiver will be sold primarily on eBay, and also on Facebook Marketplace, with instructions to download the free app from the App/Play Store.

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

The first step for the app will be helping the user to connect to their jukebox via bluetooth. It will verify that the app has bluetooth and other necessary permissions. There will be prompts to connect and turn on the receiver and jukebox if it is not detected by the phone. Once a connection is established, it will ask the user to verify the type of jukebox (vinyl or CD). There will be simple, large buttons to play a song, stop, pause (CD only). If the user taps the play button, the number keypad will pop up asking for the disc number and the song/side number. The number system is slightly different for vinyl or CD but for all versions, a number signal is being sent out corresponding to the disc.
If the user receives a call, the jukebox will stop playing. When the user hangs up the call, the music will start playing again.

### Wireframe Drawings

![Wireframe 1](https://raw.githubusercontent.com/sarahmarie23/Jukebox-App/master/wireframe1.jpg)

![Wireframe 2](https://raw.githubusercontent.com/sarahmarie23/Jukebox-App/master/wireframe2.jpg)

### Technical Architecture

* The ability to make a Bluetooth connection.
* A mapping of the jukebox buttons to the song titles. For CDs, this will be a 2 digit CD and 2 digit track number. Vinyls will just use a 2 digit value.
* Discogs API for gathering song metadata
* Storing information about the jukebox: type (vinyl or CD), name, model (for now we only have one model supported), connection status, last played song

## Challenges and Open Questions

**Getting Bluetooth working** - It seems a bit complicated and might take some time trying to get it to work. Ideally I’d get it to connect to an Arduino, since the actual final product would have one inside it. As an MVP demonstration, I could have it transmit Bluetooth data to my computer, and the data could be printed out in a terminal.

**Testing** - I won’t have a real jukebox or an actual receiver device to test the app with. As a solution, I will be simulating the app’s functionality by sending bluetooth signals to an Arduino that will then light in a certain sequence or display something on a screen. The finished app and device will work by transmitting a number that corresponds to the disc/song and the receiver will translate that number signal into the correct disc.

**Getting data** - I need to do more research on how the song information will be put into the app. There’s the Spotify API, which has access to metadata, but it has a cost after 500 requests per month. And it wouldn’t have information about records. There is the Discogs database, which includes information on vinyl records and has an API to use. I could use a website like [https://45cat.com](https://45cat.com) and get the data straight from the website.

**Using an API** - I’ve never used an API, will I be able to learn in time? Could be a knowledge limitation. To overcome this, I will start working on this early and do my best to not procrastinate.

**Question 1:** How would users prefer to make song selections? I was told to make it like a remote with number buttons. Would customers prefer that (the MVP version) or would they want something more visual like scrolling through a playlist in a music player app (Spotify)?

**Question 2:** How will the users create a playlist? The brute force method would be manually typing in each song name into a list mapped to the button number that corresponds to it in the jukebox. Ideally, they would search for the song name and then some data would be downloaded to create the playlist (album cover, artist, date). I need to get some opinions from others on what they would prefer.

# Mockup for the app

4/17/24

I have included here a page of screens that pertain to the jukebox app (actual name TBD) [Click here to view the screens.](https://raw.githubusercontent.com/sarahmarie23/Jukebox-App/master/AppMockupScreens.jpg)

I also have a flowchart to show the "golden path" of the app, [which you can check out here.](https://raw.githubusercontent.com/sarahmarie23/Jukebox-App/master/SarahMartelAppMockup.jpg)

This design is relevant to the target audience, which is jukebox collectors, because it works with the specified type of jukebox (CD or vinyl) that they own. It also gives them the value of seamless transitions to receiving a phone call while their music is playing, and resuming it afterwards, as requested by customers. It is simple and easy to use.
