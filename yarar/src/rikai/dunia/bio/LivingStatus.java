package rikai.dunia.bio;

public interface LivingStatus {
    
    public static final int UNBORN_UNDEFINED = 0;
    public static final int UNBORN_LIVING = 1;
    public static final int UNBORN_DEAD = 2;
    public static final int LIVING_UNDEFINED = 10;
    public static final int LIVING = 11;
    public static final int HIBERNATING = 12;
    public static final int DEAD = 80;
    
    public int getLivingStatus();
    public void setLivingStatus(int livingStatus);
}
