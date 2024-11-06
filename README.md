# Guess Who's the Thief- Interactive Mystery Game

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=white)

**Guess Who's the Thief** is an interactive, AI-driven mystery game that challenges players to uncover clues, interrogate suspects, and solve a high-stakes art theft case. This project combines gameplay with AI-powered dialogue and voice interaction, creating an engaging experience for players.

## Key Features
- **AI-Powered Dialogue**: Uses OpenAI's Chat Completions to provide unique, interactive feedback based on the player's choices.
- **Timer Management**: Real-time countdown timers across scenes to keep the gameplay exciting and synchronized.
- **Interactive Cutscenes**: Smooth transitions between dialogues with auto-skip functionality.
- **Sound Integration**: Custom MP3 playback for sound effects during gameplay and cutscenes.

## Gameplay Controls
- **Movement**: Mouse and keyboard interactions to select suspects and submit answers.
- **Key Bindings**:
  - **Skip Cutscene**: `Enter` key skips current dialogue.
  - **Submit Answer**: Press the `Submit` button or hit `Enter` to confirm your guess.
  - **Interact**: Click on suspects or clues to investigate them.

## API Setup Instructions
To set up the API to access Chat Completions and Text-to-Speech (TTS), follow these steps:

1. In the root of the project (same level as `pom.xml`), create a file named `apiproxy.config`.
2. Add the following credentials to the file (replace with your own):

```txt
email: "YOUR_UPI@aucklanduni.ac.nz"
apiKey: "YOUR_KEY"
```
These credentials will allow you to invoke the APIs. Keep in mind the token usage:
	•	1 token per character for Google “Standard” TTS.
	•	4 tokens per character for Google “WaveNet” and “Neural2” TTS.
	•	1 token per character for OpenAI Text-to-Text.
	•	1 token per token for OpenAI Chat Completions (charged for both input and output tokens).

## External Libraries and APIs

- **OpenAI API**: Used for generating dynamic dialogue responses and clues in the game.
- **Google TTS API**: Integrated for converting the text dialogues into voice prompts.
- **JLayer (JavaZoom)**: A library for MP3 playback, used for playing background music and sound effects.
- **JavaFX**: Utilized for the entire UI and game scene management.
- **Maven**: Manages project dependencies and build processes.
## Controls

- **WASD / Arrow keys**: Navigate character or menu.
- **Enter**: Submit responses or confirm selections.
- **Escape**: Open the pause menu.
- **Mouse**: Select suspects or interact with clues.

## Team Collaboration

| Team Member     | Role                             | GitHub                                          |
| --------------- | -------------------------------- | ----------------------------------------------- |
| Johnson Zhang     | Backend Developer & Game Logic      | [@ZingZing001](https://github.com/ZingZing001) |
| Kimberly Zhu        | Frontend AI Integration & UI/UX Design | [@kimkimz](https://github.com/kimkimz)|
| Nicky Tian   | UI/UX Design & Dialogue System  | [@Nicky8566](https://github.com/nicky8566)|

## Running the Game

1. Clone the repository to your local machine.
2. Ensure you have a valid `apiproxy.config` file with API credentials.
3. Open the project in your preferred IDE.
4. Build and run the project using Maven.

## License
This project is licensed under the MIT License.
