public class Household {

    private String householdId;
    private int occupants;

    public Household(String householdId, int occupants) {
        this.householdId = householdId;
        this.occupants = occupants;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public int getOccupants() {
        return occupants;
    }
}