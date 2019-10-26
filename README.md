# HengeStoners

HengeStoners is a mobile application designed for users to manage the location and other data of a hillforts around the world. 
Developed on a Pixel 1 Emulator.

### How to Use

This application is not on the google playstore, but if you want to clone the repo and give it a go you can. However to get google maps running you will need your own maps api key which you can get [here](https://developers.google.com/maps/documentation). Add this to the googlemapsapi.xml file in the res/values folder. 

To use HengeStoners you must first create an account by pressing the register button. This will take you to a page where you can create an account with an email, username and password. Once you have created your account you can login and start creating hillforts.

The first thing you will see on signing in is the hillfort list view. This will be empty but you can start filling it out by adding hillforts.

To Start adding press the plus icon on the tool bar. This will take you to the create hillfort menu where you can add:

- A Title
- A Description
- Set a location with google maps
- Tick a box to indicate if you have visted the location and if so a field where you can save what date you visted
- Add unlimited images
- Add unlimited Notes

Once you are satisfied with you hillfort, press the add hillfort and you will see it in your list activity. 

To edit or delete the hillfort, simply press on it in the list activity and you will be taken back to the hillfort menu with all of the data for you to change or delete.

Pressing the I icon on the tool bar will open the navigaion menu from there you will be able to access the settings menu. From there you can update your username, email and password. As well as view a number of stats about the hillfort application. If you scroll down to the bottom you will be able to delete you account entirely. 

Also on the navigation menu there is a button to logout. Pressing this will log you out from your account.

All data is stored locally on the phone in a json file. However user passwords are encrpted.
