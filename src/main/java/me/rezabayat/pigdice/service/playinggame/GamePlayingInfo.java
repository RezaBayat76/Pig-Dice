package me.rezabayat.pigdice.service.playinggame;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class GamePlayingInfo {

    private Map<Long, UserGameInfo> playersGameInfo = new HashMap<>();
}



