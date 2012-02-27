package rikai.dunia.bio;

import rikai.kuptimor.Kuptimor;

public class LifeForm extends Kuptimor implements Gender, LivingStatus {
    
    protected boolean supernatural = false;
    protected int gender;
    protected int livingStatus;
    
    
    @Override
    public int getLivingStatus() {
	return livingStatus;
    }


    @Override
    public void setLivingStatus(int livingStatus) {
	this.livingStatus = livingStatus;
    }


    @Override
    public int getGender() {
	return gender;
    }


    @Override
    public void setGender(int gender) {
	this.gender = gender;
    }


    protected boolean isSupernatural() {
	return supernatural;
    }
    

    protected boolean setSupernatural(boolean supernatural) {
	return this.supernatural = supernatural;
    }
    

}
