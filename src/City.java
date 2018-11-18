
public class City {
    private int x;
    private int y;
    private String cityName;            

    //Constructor
    //creates a city given its name and (x,y) location
	public City(String cityName, int x, int y){
    	this.cityName = cityName;
        this.x = x;
        this.y = y;
    }            
        
   
	public int getX() {
		return x;
	}

	
	public void setX(int x) {
		this.x = x;
	}

	
	public int getY() {
		return y;
	}

	
	public void setY(int y) {
		this.y = y;
	}

	public String getCityName() {
		return cityName;
	}

	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
}
