# Connect Four Game:
Connect Four is a classic two-player connection game where players take turns dropping
coloured discs into a vertical grid. The objective is to be the first player to form a
horizontal, vertical, or diagonal line of four of one’s own discs. The game is simple to learn
but offers deep strategic possibilities, making it popular among players of all ages.
This is an individual or group assignment (a group of a maximum of four (4) members).
The maximum mark is placed right after each requirement. In the marking criteria, there
are six levels of implementation: zero attempts (0% -10% complete), novice (efforts
shown – around 10%-30% are completed), promising (around 30%-50% are completed),
intermediate (about 50%-70% of the requirements are achieved), competent (about
70%-90% of the requirements are achieved), and proficient (90% or above level are
achieved).

## Functional Requirements of Your Application:
### 1. Implement Multiple Game Modes (2-player, and vs. AI) [4 Marks]:
  - In 2-player mode, two human players take turns playing against each
other on the same device.
  - In AI mode, a human player plays against an AI opponent. The AI should
be basic, making random valid moves.
### 2. Enable Personalization in the Connect Four Game [4 Marks]: Allow users to
customize game rules, such as grid size, colour themes, and player names. This
feature enhances the user experience by accommodating different preferences.
The application should provide options to customize the following settings:
  - Grid Size: Allow users to choose from standard (7x6), small (6x5), and
large (8x7) grid sizes.
  - Player Colours: Let users choose their preferred disc colours.
  - Player Names: Allow users to enter custom names for Player 1 and
Player 2.
### 3. User Profile Creation and Avatar Selection [2 Marks]: The users will be able to
create their profile, including their name and select their avatar from a collection
of avatar images. The application should save this information locally, but it is
acceptable if the data is lost when the app closes. Allow users to edit their profile
settings, including avatar selection.
### 4. Gameplay Statistics Tracking [3 Marks]: The application should track gameplay
statistics, such as total games played, wins, losses, and win percentages for each
user. This information can be volatile (lost when the app closes).
### 5. In-game Information [10 Marks]: While playing Connect Four, players should
have access to various pieces of information that enhance their understanding of
the game state, their opponent’s moves, and their progress. Essential information
includes:
  - Game Board: The main visual representation of the game, showing the
current state of the grid. Clearly marked cells should indicate where
each player has placed their discs.
  - Player Indicators: Display the names or labels of the players (e.g.,
“Player 1” and “Player 2”). Indicate the current player’s turn.
  - Game Progress: Display the number of moves made and any
remaining moves.
  - Notifications and Messages: Provide informative messages or
notifications about important game events, such as a win, draw, or
invalid move. Display messages indicating the game’s outcome, such
as “Player X wins!” or “It’s a draw!”
  - Undo or Reset Option: Allow players to undo their last move or reset
the game board if they make a mistake or want to start over.
  - Settings and Menu: Provide access to settings, allowing players to
adjust customization options or return to the main menu. Include a
way to pause or exit the game.
  - User Profile: Display the user’s profile information, including their
avatar and username.
### 6. Special Technical Requirements: There are different ways to achieve the above
requirements (from naive to advanced approaches). The following requirements
ensure that the application follows standard software design principles:
  - Adaptive UI [2 Marks]: Ensure that the UI layout and components
adapt seamlessly to both landscape and portrait orientations and
across different device types (phones and tablets). Test and adjust UI
elements to avoid overlapping or misalignment when the device
orientation changes.
  - State Preservation [2 Mark]: Ensure that in-game information is not
lost when the orientation changes.
  - Modular UI Architecture [3 Marks]: Design and implement the
application using a modular architecture that cleanly separates
different UI components and their management. Each major feature or
screen (such as the main menu, settings, user profile, and game
interface) should be encapsulated in its own module or component.
Ensure that these components are independent, reusable, and can
manage their own lifecycle efficiently. The architecture should support
seamless navigation between components and maintain a consistent
state across the application, enabling a flexible and maintainable
structure that can scale with future features and updates.
