package com.example.root;

public class userDataModel {
    String userName,userId,userEmail,
            userMobile,userPicUrl,userDOB,userGender,UserAddedAmount,
            UserWinningAmount,UserCashBonus,FromUserId,ReferralCode;

    public userDataModel(String userName, String userId, String userEmail, String userMobile, String userPicUrl, String userDOB, String userGender, String userAddedAmount, String userWinningAmount, String userCashBonus, String fromUserId, String referralCode) {
        this.userName = userName;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userMobile = userMobile;
        this.userPicUrl = userPicUrl;
        this.userDOB = userDOB;
        this.userGender = userGender;
        this.UserAddedAmount = userAddedAmount;
        this.UserWinningAmount = userWinningAmount;
        this.UserCashBonus = userCashBonus;
        this.FromUserId = fromUserId;
        this.ReferralCode = referralCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAddedAmount() {
        return UserAddedAmount;
    }

    public void setUserAddedAmount(String userAddedAmount) {
        UserAddedAmount = userAddedAmount;
    }

    public String getUserWinningAmount() {
        return UserWinningAmount;
    }

    public void setUserWinningAmount(String userWinningAmount) {
        UserWinningAmount = userWinningAmount;
    }

    public String getUserCashBonus() {
        return UserCashBonus;
    }

    public void setUserCashBonus(String userCashBonus) {
        UserCashBonus = userCashBonus;
    }

    public String getFromUserId() {
        return FromUserId;
    }

    public void setFromUserId(String fromUserId) {
        FromUserId = fromUserId;
    }

    public String getReferralCode() {
        return ReferralCode;
    }

    public void setReferralCode(String referralCode) {
        ReferralCode = referralCode;
    }
}
