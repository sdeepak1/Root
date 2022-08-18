package com.example.root;

import java.util.ArrayList;

public class Winner_show_model {

    String Tournament_Name,Tournament_Date,Tournament_Time,Tournament_MatchType,Tournament_MatchMode,
    Tournament_WinnerAmount,Tournament_Id;

    ArrayList<WinnerUserModel> winnerUserList;

    public Winner_show_model(String tournament_Name, String tournament_Date, String tournament_Time, String tournament_MatchType, String tournament_MatchMode, String tournament_WinnerAmount, String tournament_Id, ArrayList<WinnerUserModel> winnerUserList) {
        Tournament_Name = tournament_Name;
        Tournament_Date = tournament_Date;
        Tournament_Time = tournament_Time;
        Tournament_MatchType = tournament_MatchType;
        Tournament_MatchMode = tournament_MatchMode;
        Tournament_WinnerAmount = tournament_WinnerAmount;
        Tournament_Id = tournament_Id;
        this.winnerUserList = winnerUserList;
    }

    public String getTournament_Name() {
        return Tournament_Name;
    }

    public void setTournament_Name(String tournament_Name) {
        Tournament_Name = tournament_Name;
    }

    public String getTournament_Date() {
        return Tournament_Date;
    }

    public void setTournament_Date(String tournament_Date) {
        Tournament_Date = tournament_Date;
    }

    public String getTournament_Time() {
        return Tournament_Time;
    }

    public void setTournament_Time(String tournament_Time) {
        Tournament_Time = tournament_Time;
    }

    public String getTournament_MatchType() {
        return Tournament_MatchType;
    }

    public void setTournament_MatchType(String tournament_MatchType) {
        Tournament_MatchType = tournament_MatchType;
    }

    public String getTournament_MatchMode() {
        return Tournament_MatchMode;
    }

    public void setTournament_MatchMode(String tournament_MatchMode) {
        Tournament_MatchMode = tournament_MatchMode;
    }

    public String getTournament_WinnerAmount() {
        return Tournament_WinnerAmount;
    }

    public void setTournament_WinnerAmount(String tournament_WinnerAmount) {
        Tournament_WinnerAmount = tournament_WinnerAmount;
    }

    public String getTournament_Id() {
        return Tournament_Id;
    }

    public void setTournament_Id(String tournament_Id) {
        Tournament_Id = tournament_Id;
    }

    public ArrayList<WinnerUserModel> getWinnerUserList() {
        return winnerUserList;
    }

    public void setWinnerUserList(ArrayList<WinnerUserModel> winnerUserList) {
        this.winnerUserList = winnerUserList;
    }
}