package com.hakan.homes;

import com.hakan.homes.utils.yaml.Yaml;

import java.util.Map;

public class PlayerData {

    private final String playerName;
    private Map<String, Home> homeList;
    private Yaml dataFile;

    public PlayerData(String playerName, Map<String, Home> homeList, Yaml dataFile) {
        this.playerName = playerName;
        this.homeList = homeList;
        this.dataFile = dataFile;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Map<String, Home> getHomeList() {
        return this.homeList;
    }

    public void setHomeList(Map<String, Home> homeList) {
        this.homeList = homeList;
    }

    public boolean hasHome(String homeName) {
        return getHome(homeName) != null;
    }

    public Home getHome(String homeName) {
        return this.homeList.getOrDefault(homeName, null);
    }

    public void setHome(String homeName, Home home) {
        this.homeList.put(homeName, home);
    }

    public void addHome(Home home) {
        this.homeList.put(home.getHomeName(), home);
    }

    public void removeHome(Home home) {
        this.homeList.remove(home.getHomeName());
    }

    public Yaml getDataFile() {
        return this.dataFile;
    }

    public void setDataFile(Yaml dataFile) {
        this.dataFile = dataFile;
    }
}