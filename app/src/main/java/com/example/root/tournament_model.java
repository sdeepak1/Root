package com.example.root;

import java.util.ArrayList;
import java.util.List;

public class tournament_model {
    String Tournament_Name, Tournament_Id, Tournament_Game_Name, Tournament_Date, Tournament_Time;
    int Tournament_Winner_Amount, Tournament_Entry_Fee;
    String Tournament_Match_Type, Tournament_Match_Mode, Tournament_Match_Map;
    int Tournament_Limit_Person, Tournament_Mode_Value;
    ArrayList<String> getAllJoinUserSize;
    int join;
    String roomId, roomPassword;
    List<showAmountModel> getAllWinnerAmount;
    int joinNotJoin;


    public tournament_model(String tournament_Name, String tournament_Id, String tournament_Game_Name, String tournament_Date, String tournament_Time, int tournament_Winner_Amount, int tournament_Entry_Fee, String tournament_Match_Type, String tournament_Match_Mode, String tournament_Match_Map, int tournament_Limit_Person, int tournament_Mode_Value, ArrayList<String> getAllJoinUserSize, int join, String roomId, String roomPassword, List<showAmountModel> getAllWinnerAmount,int joinNotJoin) {
        Tournament_Name = tournament_Name;
        Tournament_Id = tournament_Id;
        Tournament_Game_Name = tournament_Game_Name;
        Tournament_Date = tournament_Date;
        Tournament_Time = tournament_Time;
        Tournament_Winner_Amount = tournament_Winner_Amount;
        Tournament_Entry_Fee = tournament_Entry_Fee;
        Tournament_Match_Type = tournament_Match_Type;
        Tournament_Match_Mode = tournament_Match_Mode;
        Tournament_Match_Map = tournament_Match_Map;
        Tournament_Limit_Person = tournament_Limit_Person;
        Tournament_Mode_Value = tournament_Mode_Value;
        this.getAllJoinUserSize = getAllJoinUserSize;
        this.join = join;
        this.roomId = roomId;
        this.roomPassword = roomPassword;
        this.getAllWinnerAmount = getAllWinnerAmount;
        this.joinNotJoin=joinNotJoin;
    }

    public String getTournament_Name() {
        return Tournament_Name;
    }

    public void setTournament_Name(String tournament_Name) {
        Tournament_Name = tournament_Name;
    }

    public String getTournament_Id() {
        return Tournament_Id;
    }

    public void setTournament_Id(String tournament_Id) {
        Tournament_Id = tournament_Id;
    }

    public String getTournament_Game_Name() {
        return Tournament_Game_Name;
    }

    public void setTournament_Game_Name(String tournament_Game_Name) {
        Tournament_Game_Name = tournament_Game_Name;
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

    public int getTournament_Winner_Amount() {
        return Tournament_Winner_Amount;
    }

    public void setTournament_Winner_Amount(int tournament_Winner_Amount) {
        Tournament_Winner_Amount = tournament_Winner_Amount;
    }

    public int getTournament_Entry_Fee() {
        return Tournament_Entry_Fee;
    }

    public void setTournament_Entry_Fee(int tournament_Entry_Fee) {
        Tournament_Entry_Fee = tournament_Entry_Fee;
    }

    public String getTournament_Match_Type() {
        return Tournament_Match_Type;
    }

    public void setTournament_Match_Type(String tournament_Match_Type) {
        Tournament_Match_Type = tournament_Match_Type;
    }

    public String getTournament_Match_Mode() {
        return Tournament_Match_Mode;
    }

    public void setTournament_Match_Mode(String tournament_Match_Mode) {
        Tournament_Match_Mode = tournament_Match_Mode;
    }

    public String getTournament_Match_Map() {
        return Tournament_Match_Map;
    }

    public void setTournament_Match_Map(String tournament_Match_Map) {
        Tournament_Match_Map = tournament_Match_Map;
    }

    public int getTournament_Limit_Person() {
        return Tournament_Limit_Person;
    }

    public void setTournament_Limit_Person(int tournament_Limit_Person) {
        Tournament_Limit_Person = tournament_Limit_Person;
    }

    public int getTournament_Mode_Value() {
        return Tournament_Mode_Value;
    }

    public void setTournament_Mode_Value(int tournament_Mode_Value) {
        Tournament_Mode_Value = tournament_Mode_Value;
    }

    public ArrayList<String> getGetAllJoinUserSize() {
        return getAllJoinUserSize;
    }

    public void setGetAllJoinUserSize(ArrayList<String> getAllJoinUserSize) {
        this.getAllJoinUserSize = getAllJoinUserSize;
    }

    public int getJoin() {
        return join;
    }

    public void setJoin(int join) {
        this.join = join;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public List<showAmountModel> getGetAllWinnerAmount() {
        return getAllWinnerAmount;
    }

    public void setGetAllWinnerAmount(List<showAmountModel> getAllWinnerAmount) {
        this.getAllWinnerAmount = getAllWinnerAmount;
    }

    public int getJoinNotJoin() {
        return joinNotJoin;
    }

    public void setJoinNotJoin(int joinNotJoin) {
        this.joinNotJoin = joinNotJoin;
    }
}
