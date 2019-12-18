package com.example.metrobustracker.Model;

public class Routes {

    private String ProviderID;
    private String Description;
    private String Route;

    public String getProviderID ()
    {
        return ProviderID;
    }

    public void setProviderID (String ProviderID)
    {
        this.ProviderID = ProviderID;
    }

    public String getDescription ()
    {
        return Description;
    }

    public void setDescription (String Description)
    {
        this.Description = Description;
    }

    public String getRoute ()
    {
        return Route;
    }

    public void setRoute (String Route)
    {
        this.Route = Route;
    }

    @Override
    public String toString() {
        return "Routes{" +
                "ProviderID='" + ProviderID + '\'' +
                ", Description='" + Description + '\'' +
                ", Route='" + Route + '\'' +
                '}';
    }

}
