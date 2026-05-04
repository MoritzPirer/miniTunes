# miniTunes
miniTunes is an intentionally minimal radio-style music player using a text based interface. 
It's intended to provide simple control over background music for working, studying or relaxing

## Usage
Follow The Setup guide below to install miniTunes. Once open, you can make use of a few simple control commands:
- q: quit the application
- s: skip to the next song
- b: go back to the previous song
- r: restart the current song
- p: play or pause
- c: change the channel

miniTunes doesn't come preloaded with music ~~because I don't want to deal with copyright~~ to encourage you to collect your own
unique music collection. Upon starting the app for the first time, it automatically creates a folder names "miniTunes" in your home folder.
Every folder you add to it will act as a "radio channel" you can switch between. Note that miniTunes only supports `.mp3` files and other file types
(e.g. `.wav`) wil be ignored.

## Tech Stack
miniTunes is written in Java 21. It uses Java Lanterna displaying the interface and receiving user input, Java Zoom for audio playback and Mp3gic to parse file metadata.
Dependencies are managed using maven.

## Setup & Installation

### Just use

#### macOS
- Select the latest release from `releases/macOS`. It should be a `.dmg` file.
- Drag the miniTunes icon into the Applications folder
- When the app is launched for the first time, It will create a folder `miniTunes` in your home folder (you can also create this manually).
- Place folders with `.mp3` files there and restart the app

### Windows
TODO

### Linux
TODO

## Build from source
- Clone the repository
- Ensure maven and Java 21 are installed 
- Compile the Fat JAR using the command `mvn clean package` from the project's root folder
- generate a clickable file using `jpackage`:

**macOS**:
```
jpackage --input target/ \
--name "miniTunes" \
--main-jar Radio-1.0-SNAPSHOT.jar \
--main-class Radio \
--type dmg \
--icon assets/icon.icns
```

**Windows**:
TODO

**Linux**:
TODO
