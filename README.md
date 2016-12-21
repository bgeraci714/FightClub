# FightClub
A Pokemon Battle (Fight) Club Scheduler

A project developed and created by Blake Geraci, Isabelle Griffin, and Bri Velozo for CSI 254: Java Programming I. 

The project includes a Javadoc located in the dist folder (and accessed via the index.html page). 

# What the project does: 
  Allows you to schedule "battles" between two opponents at a designated time and date. The scheduler is also able to determine what bracket (or usage tier of Pokemon) are allowed to be used. More information can be found [here](http://bulbapedia.bulbagarden.net/wiki/Tier). Dates are very flexibly chosen and the battles for a given month are displayed on a panel visible on the right side of the Calendar frame. 
  
  # Organization: 
    - The front end is built using Java's Swing GUI framework built using the NetBeans editor. 
		- The back end is built using the core Java language with File IO utilities that enable relative path usage and more dynamic file reading/writing. At it's core, the battles are stored as Battle objects within a instance of the Schedule class. The schedule class holds the functions for File IO as well as a hashtable (with linked lists as the values) to allow for a dynamically sized storage space. The quick accessibility of the hashtable is further enhanced by the flexibility of the linked lists. 
