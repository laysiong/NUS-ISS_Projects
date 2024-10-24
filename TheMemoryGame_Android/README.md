# TheMemoryGame_Android

## Overview
**TheMemoryGame_Android** is a memory card-matching game developed over 2-3 weeks by a team of six. The game allows players to flip cards and try to match identical images across two activities. Players can fetch images from a specified URL, select 6 of them, and then play the memory game.

## Technologies Used


### Mobile
- **Android Studio (Java)**: Native mobile application development for Android devices.
- **Android UI Components**: Grid layout for image selection and game interaction.
- **ProgressBar**: Used to indicate image download progress.
- **Animations**: Custom animations for matching and mismatching cards.
  
## Features
- **Fetch Images from URL**: Allows users to specify a URL and fetch the first 20 images from the webpage. A progress bar displays download progress.
- **Grid Selection**: Users select 6 images from the downloaded images to start the game.
- **Game Play**: 
   - Displays 12 placeholders (two for each selected image). When a placeholder is touched, it reveals the image.
   - If two selected images match, they remain visible. If they don't, they revert to placeholders after a delay.
- **Two-Player Mode**: Players take turns, and a winner is declared at the end based on who matches more pairs.
- **Sound Effects and Animations**: Special sound effects and animations play for matches and mismatches.


## Usage
Once the app is running, users can input a URL to fetch images, select 6 images, and begin the game. Players can take turns in two-player mode, and the winner is determined based on the number of matched pairs.

## Contributors
This project was developed by a team of six members. I specifically ensured the ability to fetch and select images, implemented the two-player mode, and added sound effects and animations.
