package rikai.dunia.bio;

public interface Gender {
    public static final int GENDER_UNDEFINED = 0;
    public static final int GENDER_NONE = 1;
    public static final int GENDER_BOTH = 2;
    public static final int GENDER_MALE = 3;
    public static final int GENDER_FEMALE = 4;
    
    public int getGender();
    public void setGender(int gender);
}
