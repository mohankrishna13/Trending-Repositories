# Trending-Repositories

<b>Libraries & Dependencies:</b>
  1. Glide
  2. Retrofit
  3. Room
  4. OkHttp
  5. SwipeLayout
  6. kotlin-coroutines
  7. circleimageview
  
 <b>Architecture:</b> MVVM
  
 1. Created an Activtiy (Main Activity) that displays Trending Repositories from API.
 2. For API calling Retrofit library is used .
     * If the response is successful the data will be stored in room database for offline availabilty.
     * If not, appropriate message will be shown.
 3. If there is no Internet available it will check Room Database whether there is any data present or not
    * if data present it wll shows in Main Activity.
    * If No Data present it will "No Internet Connection " layout.
 4. Users can refresh the screen to see latest repositories if any.
 5. User can search for specific or any desired repository using search view on toolbar of MainActivity.`
 
<b> Other Inclusions:</b>
 
 1. If user selects any respository from recyclerview new screen will get displayed along with repository details and builder
 2. you can find more details about the builder by clicking the link provided.
 3. you can find complete repository details by clicking the link provided at the bottom layout(Repository URL)
  
 
