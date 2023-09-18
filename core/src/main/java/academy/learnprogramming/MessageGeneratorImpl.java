package academy.learnprogramming;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageGeneratorImpl implements MessageGenerator {
     // == constants ==
    public static final String MAIN_MESSAGE = "game.core.main-msg";
    public static final String FIRST_GUESS_MESSAGE = "game.core.result-msg-first-guess";
    public static final String WRONG_RANGE_MESSAGE = "game.core.result-msg-wrong-range";
    public static final String WON_MESSAGE = "game.core.result-msg-won";
    public static final String LOST_MESSAGE = "game.core.result-msg-lost";
    public static final String HIGHER_MESSAGE = "game.core.result-msg-higher";
    public static final String LOWER_MESSAGE = "game.core.result-msg-lower";
    public static final String REMAINING_GUESSES_MESSAGE = "game.core.result-msg-remaining-guesses";
    public static final String LAST_GUESS_MESSAGE = "game.core.result-msg-last-guess";

     // == fields ==
    private final Game game;
    private final MessageSource messageSource;

    // == constructor ==
    @Autowired
    public MessageGeneratorImpl(Game game, MessageSource messageSource) {
        this.game = game;
        this.messageSource = messageSource;
    }
    // == init ==
    @PostConstruct
    public void init() {
        log.info("game: {}", game);
    }

    // == public methods ==
    @Override
    public String getMainMessage() {
        return getMessage(MAIN_MESSAGE, game.getSmallest(), game.getBiggest());
    }

    @Override
    public String getResultMessage() {

        if (game.isGameWon()) {
            //return "You guessed it! The number was " + game.getNumber();
            return getMessage(WON_MESSAGE, game.getNumber());
        } else if (game.isGameLost()) {
            //return "You lost. The number was " + game.getNumber();
            return getMessage(LOST_MESSAGE, game.getNumber());
        } else if (!game.isValidNumberRange()) {
            //return "Invalid number range!";
            return getMessage(WRONG_RANGE_MESSAGE);
        } else if (game.getRemainingGuesses() == game.getGuessCount()) {
            //return "What is your first guess?";
            return getMessage(FIRST_GUESS_MESSAGE);
        } else {
            String direction = getMessage(LOWER_MESSAGE);

            if (game.getGuess() < game.getNumber()) {
                direction = getMessage(HIGHER_MESSAGE);
            }

            if (game.getRemainingGuesses() == 1) {
                //return direction + "! You have only 1 guess left!";
                return getMessage(LAST_GUESS_MESSAGE, direction);
            } else {
                //return direction + "! You have " + game.getRemainingGuesses() + " guesses left!";
                return getMessage(REMAINING_GUESSES_MESSAGE, direction, game.getRemainingGuesses());
            }
        }
    }

    // == private methods ==
    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
