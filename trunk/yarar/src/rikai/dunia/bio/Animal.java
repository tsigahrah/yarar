package rikai.dunia.bio;

public class Animal extends LifeForm {
    
    public boolean isMale() {
	return getGender() == Gender.GENDER_MALE;
    }

    public boolean isFemale() {
	return getGender() == Gender.GENDER_FEMALE;
    }
    
    public boolean isBorn() {
	return getLivingStatus() >= LivingStatus.LIVING_UNDEFINED;
    }
    
    public boolean isDead() {
	return getLivingStatus() >= LivingStatus.DEAD;
    }
    
    public boolean isLiving() {
	int ls = getLivingStatus();
	return (ls == LivingStatus.UNBORN_LIVING || ls == LivingStatus.LIVING_UNDEFINED || ls == LivingStatus.LIVING || ls == LivingStatus.HIBERNATING);
    }
}
