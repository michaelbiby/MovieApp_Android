// MainActivity.java
// Hosts Address Book app's fragments
package com.trinity.moviecollection;

import com.trinity.moviecollection.R;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends Activity 
   implements MovieListFragment.MovieListFragmentListener,
      DetailsFragment.DetailsFragmentListener, 
      AddEditFragment.AddEditFragmentListener
{
   // keys for storing row ID in Bundle passed to a fragment
   public static final String ROW_ID = "row_id"; 
   
   MovieListFragment movieListFragment; // displays movie list
   
   // display movieListFragment when MainActivity first loads
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // return if Activity is being restored, no need to recreate GUI
      if (savedInstanceState != null) 
         return;

      // check whether layout contains fragmentContainer (phone layout);
      // movieListFragment is always displayed
      if (findViewById(R.id.fragmentContainer) != null) 
      {
         // create movieListFragment
    	  movieListFragment = new MovieListFragment();
         
         // add the fragment to the FrameLayout
         FragmentTransaction transaction = 
            getFragmentManager().beginTransaction();
         transaction.add(R.id.fragmentContainer, movieListFragment);
         transaction.commit(); // causes movieListFragment to display
      }
   }
   
   // called when MainActivity resumes
   @Override
   protected void onResume()
   {
      super.onResume();
      
      // if movieListFragment is null, activity running on tablet, 
      // so get reference from FragmentManager
      if (movieListFragment == null)
      {
    	  movieListFragment = 
            (MovieListFragment) getFragmentManager().findFragmentById(
               R.id.contactListFragment);      
      }
   }
   
   // display DetailsFragment for selected movie
   @Override
   public void onMovieSelected(long rowID)
   {
      if (findViewById(R.id.fragmentContainer) != null) // phone
         displaymovie(rowID, R.id.fragmentContainer);
      else // tablet
      {
         getFragmentManager().popBackStack(); // removes top of back stack
         displaymovie(rowID, R.id.rightPaneContainer);
      }
   }

   // display a movie
   private void displaymovie(long rowID, int viewID)
   {
      DetailsFragment detailsFragment = new DetailsFragment();
      
      // specify rowID as an argument to the DetailsFragment
      Bundle arguments = new Bundle();
      arguments.putLong(ROW_ID, rowID);
      detailsFragment.setArguments(arguments);
      
      // use a FragmentTransaction to display the DetailsFragment
      FragmentTransaction transaction = 
         getFragmentManager().beginTransaction();
      transaction.replace(viewID, detailsFragment);
      transaction.addToBackStack(null);
      transaction.commit(); // causes DetailsFragment to display
   }
   
   // display the AddEditFragment to add a new movie
   @Override
   public void onAddMovie()
   {
      if (findViewById(R.id.fragmentContainer) != null)
         displayAddEditFragment(R.id.fragmentContainer, null); 
      else
         displayAddEditFragment(R.id.rightPaneContainer, null);
   }
   
   // display fragment for adding a new or editing an existing movie
   private void displayAddEditFragment(int viewID, Bundle arguments)
   {
      AddEditFragment addEditFragment = new AddEditFragment();
      
      if (arguments != null) // editing existing movie
         addEditFragment.setArguments(arguments);
      
      // use a FragmentTransaction to display the AddEditFragment
      FragmentTransaction transaction = 
         getFragmentManager().beginTransaction();
      transaction.replace(viewID, addEditFragment);
      transaction.addToBackStack(null);
      transaction.commit(); // causes AddEditFragment to display
   }
   
   // return to movie list when displayed movie deleted
   @Override
   public void onMovieDeleted()
   {
      getFragmentManager().popBackStack(); // removes top of back stack
      
      if (findViewById(R.id.fragmentContainer) == null) // tablet
    	  movieListFragment.updateMovieList();
   }

   // display the AddEditFragment to edit an existing movie
   @Override
   public void onEditMovie(Bundle arguments)
   {
      if (findViewById(R.id.fragmentContainer) != null) // phone
         displayAddEditFragment(R.id.fragmentContainer, arguments); 
      else // tablet
         displayAddEditFragment(R.id.rightPaneContainer, arguments);
   }

   // update GUI after new movie or updated movie saved
   @Override
   public void onAddEditCompleted(long rowID)
   {
      getFragmentManager().popBackStack(); // removes top of back stack

      if (findViewById(R.id.fragmentContainer) == null) // tablet
      {
         getFragmentManager().popBackStack(); // removes top of back stack
         movieListFragment.updateMovieList(); // refresh movies

         // on tablet, display movie that was just added or edited
         displaymovie(rowID, R.id.rightPaneContainer); 
      }
   }   
}


