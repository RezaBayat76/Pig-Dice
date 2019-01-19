package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.CommentOnGameDTO;
import me.rezabayat.pigdice.dto.GameCommentDTO;
import me.rezabayat.pigdice.dto.GameDTO;
import me.rezabayat.pigdice.dto.UpdatedScoreDTO;
import me.rezabayat.pigdice.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("games")
@CrossOrigin
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameDTO> games() {
        return this.gameService.games();
    }

    @PostMapping("add-game")
    public void addGame(@RequestBody GameDTO gameDTO, @RequestHeader("Authorization") String token) {
        this.gameService.addGame(gameDTO, token);
    }

    @PostMapping("add-comment")
    public void addComment(@RequestBody CommentOnGameDTO commentOnGameDTO, @RequestHeader("Authorization") String token) {
        this.gameService.addComment(commentOnGameDTO, token);
    }

    @GetMapping("comments/{id}")
    public List<GameCommentDTO> comments(@PathVariable("id") long id) {
        return this.gameService.comments(id);
    }

    @GetMapping("{id}")
    public GameDTO getGame(@PathVariable("id") long id) {
        return this.gameService.getGame(id);
    }

    @GetMapping("best-games")
    public List<GameDTO> bestGames() {
        return this.gameService.bestGames();
    }

    @GetMapping("most-playing")
    public List<GameDTO> mostPlaying() {
        return this.gameService.mostPlaying();
    }

    @GetMapping("best-recently")
    public List<GameDTO> bestRecently() {
        return this.gameService.bestRecently();
    }

    @PostMapping("update-score")
    public void updateGameScore( @RequestBody UpdatedScoreDTO updatedScoreDTO) {
        this.gameService.updateScore(updatedScoreDTO);
    }
}
