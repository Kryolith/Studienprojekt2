package studienprojekt;

import java.util.ArrayList;
import java.util.List;
import studienprojekt.osm.OSMCoordinate;
import studienprojekt.osm.OSMWay;

public class Result {
    public static int counter = 1;
    private int id;
    private String name;
    private List<OSMWay> ways;
    private SpaceUsageRule spaceUsageRule;
    private OSMCoordinate osmCoordinate;
    
    public Result() {
        this.id = counter++;
        this.name = "unnamed";
        this.ways = new ArrayList();
    }
    
    public void setOSMWays(List<OSMWay> ways) {
        this.ways = ways;
    }
    
    public List<OSMWay> getOSMWays() {
        return this.ways;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public SpaceUsageRule getSpaceUsageRule() {
        return spaceUsageRule;
    }

    public void setSpaceUsageRule(SpaceUsageRule spaceUsageRule) {
        this.spaceUsageRule = spaceUsageRule;
    }

    public OSMCoordinate getOSMCoordinate() {
        return osmCoordinate;
    }

    public void setOSMCoordinate(OSMCoordinate osmCoordinate) {
        this.osmCoordinate = osmCoordinate;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
