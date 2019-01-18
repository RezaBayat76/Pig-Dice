package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.CommentOnPlayedGame;
import me.rezabayat.pigdice.dto.PlayedGameCommentDTO;
import me.rezabayat.pigdice.service.PlayedGameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("played-games")
@CrossOrigin
public class PlayedGameController {
    private final PlayedGameService playedGameService;

    public PlayedGameController(PlayedGameService playedGameService) {
        this.playedGameService = playedGameService;
    }

    @PostMapping("add-comment")
    public void addComment(@RequestBody CommentOnPlayedGame commentOnPlayedGame, @RequestHeader("Authorization") String token) {
        this.playedGameService.addComment(commentOnPlayedGame, token);
    }

    @GetMapping("comments/{id}")
    public List<PlayedGameCommentDTO> comments(@PathVariable("id") long id) {
        return this.playedGameService.comments(id);
    }

    @GetMapping("play-game/{id}")
    public void playGame(@PathVariable("id") long id, @RequestHeader("Authorization") String token){
        this.playedGameService.playGame(id, token);
    }

    @GetMapping("roll-dice/{id}")
    public List<Long> rollDice(@PathVariable("id") long id){
        return this.playedGameService.rollDice(id);
    }

    @GetMapping("hold/{playedGameId}")
    public void hold(@PathVariable("playedGameId") long playedGameId, @RequestHeader("Authorization") String token){
        this.playedGameService.hold(playedGameId, token);
    }

}
