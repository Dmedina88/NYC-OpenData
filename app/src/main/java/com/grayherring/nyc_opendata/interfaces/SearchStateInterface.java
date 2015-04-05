package com.grayherring.nyc_opendata.interfaces;

/**
 * Created by david on 4/4/2015.
 */

//  future plan witht his  uses state patern when clicking the button that got it to that state change state back to   as ifit was not pushed
// chang text  on buttons to match  if using acton button change   likly to be done in  an abstact class consuctor?
public interface SearchStateInterface {
    public void updateUI();

    public void queriedSearchClicked();

    public void locationalSearchClicked();

    public void Search();
}
