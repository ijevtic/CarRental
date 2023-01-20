package org.example.client.notificationservice.pop;
public class PopReservation extends ANotification {
    private String clientEmail;
    private String managerEmail;
    private String clientUsername;
    private String modelName;
    private String city;
    private Long companyId;
    private String companyName;
    private Integer startTime;
    private Integer endTime;
    //TODO add price


    public PopReservation() {
    }

    public PopReservation(String clientEmail, String managerEmail, String clientUsername, String modelName, String city, Long companyId, String companyName, Integer startTime, Integer endTime) {
        this.clientEmail = clientEmail;
        this.managerEmail = managerEmail;
        this.clientUsername = clientUsername;
        this.modelName = modelName;
        this.city = city;
        this.companyId = companyId;
        this.companyName = companyName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "PopReservation{" +
                "clientEmail='" + clientEmail + '\'' +
                ", managerEmail='" + managerEmail + '\'' +
                ", clientUsername='" + clientUsername + '\'' +
                ", modelName='" + modelName + '\'' +
                ", city='" + city + '\'' +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
