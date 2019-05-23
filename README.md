# BottomNavigationActivity
> This application is designed to meet the requirement of SPHTech Mobile Applications Assignment

## MVC Architecture

### Model
- ++com.example.bottomnavigationactivity.model++

**All  data models are stored in ==model== package**

### View
- ++com.example.bottomnavigationactivity.ui++

**All Views, for example Activities , selfDesign Views are stored in ==ui== package**

### Control
1. ++com.example.bottomnavigationactivity.api.manager++

**All models are created and assembled in all kind of managers. Managers deal business logcial things**
**For example : ==ProductManager== is responsible for dealing all product things**

2. ++com.example.bottomnavigationactivity.task++

**An AsyncTask component is used for backgroud operations**

**I use a fix pattern  (==BaseActivity== + ==CommonAsyncTask== + ==OnDataListener== + ==Result==) for pulling data form Internet. Benefit from this pattern, Model , View and Controlor are decoupled**

**Operation Steps:**

**Step 1: All activities must extends BaseActivity.**

**Step 2: Call ==doConnection()== to start an AsyncTask for pulling internet data**

**Step 3: Deal background operations in recall ==doFetchData()==. It will be call in background thread**

**Step 4: Deal UI operations in recall ==doProcessData()==. It will be call in UI thread after ==doFetchData()==**

**Step 5: Deal error operations in recall ==doErrorData()==. It will be call in UI thread after ==doFetchData()== when there are some errors happened.**


3. ++com.example.bottomnavigationactivity.utils++

**Some  util class are store in ==utils== package**

**==AndroidUtils== is an android util class**

**==DBUtils== is an android database util class**

**==Common== is an  common functions util class**

## Task One: Display a list of data
Use MainActivity to display a list 
## Task Two: Display a clickable image
Display a click image if any quarter in a year demonstrates a descrease in volum data.
## Task Threee: Data Caching
An simple cache example : use SharePreferences to cache the last one URL request result.

==ProductManage.java==

==AndroidUtils.getSharedPreferencesString(context,url)== :  get cache result

==AndroidUtils.writeSharedPreferencesString(context,url ,data);==  set cache result
```

		boolean needToCache = true;
		if(null == data){
			// httprRequest Fail, get the latest cache
			needToCache = false;
			data = AndroidUtils.getSharedPreferencesString(context,url);
		}

		Result result = getResult(data,true);
		JSONObject jo = result.getJsonObject();
		Product product = new Product();
		if(result.isSucc()){
			try {

				//Cache the latest One url request
				if(needToCache){
					AndroidUtils.writeSharedPreferencesString(context,url ,data);
				}
				...
		}

```
> How to test it: Turn on wifi and open this application , you will see the MainActivity with results. Then turn off wifi and reopen the application , you will see the MainActivity with last cache results without wifi.

Another way to cache result is DataBase caching.
You can use ==DatabaseUtils.java== 


## Testing 

### AndroiUITesting
Libraries
- espresso 
- Robolectric

Android UI test is used for test Android UI operations.
Testing engineer use them for auto testing 

==ExampleInstrumentedTest.java==

```
//test ListView's gusture operation
 @Test      
    public void ensure_pullToRefreshView_exit(){
        onView(withId(R.id.pullToRefresh)).perform(ViewActions.swipeUp());
    }

//test if an ImageView exist
    @Test 
    public void ensure_ImageView_exit_and_perform_click(){


        onView(allOf(withId(R.id.iv_icon), hasSibling(withText("2012-decrease"))))
                .perform(ViewActions.click());
    }
```

### Android JUnit Testing
Libraries
- junit
- mockwebserver
- mockito

JUnit is used for normal class and functions testing.
I have write two testing class:
- CommomUnitTest.java is used for testing Commom.java 
- ModelTest.java is used for testing all the Model


==CommomUnitTest.java==
```
    /**
     * test httpGet when internet is conneted
     */
    @Test
    public void ensure_httpGet_isCorrect_when_internet_is_conneted() {

        //init MockServer
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(URL_RESPONSE));

        //return true when detect network connection
        when(mMockContext.getSystemService(mMockContext.CONNECTIVITY_SERVICE)).thenReturn( mMockConManager);
        when(mMockConManager.getActiveNetworkInfo()).thenReturn( mMockNetWorkInfo);
        when(mMockNetWorkInfo.isConnectedOrConnecting()).thenReturn(true);


        try{

            server.start();
            HttpUrl baseUrl = server.url(URL);
            String result = Commom.httpGet(URL,null,mMockContext);
            assertEquals(URL_RESPONSE,result);

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    /**
     * test httpGet when internet isn't connected
     */
    @Test
    public void ensure_httpGet_isCorrect_when_internet_not_conneted() {

        //init MockServer
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(URL_RESPONSE));

        //return true when detect network connection
        when(mMockContext.getSystemService(mMockContext.CONNECTIVITY_SERVICE)).thenReturn( mMockConManager);
        when(mMockConManager.getActiveNetworkInfo()).thenReturn( mMockNetWorkInfo);
        when(mMockNetWorkInfo.isConnectedOrConnecting()).thenReturn(false);


        try{

            server.start();
            HttpUrl baseUrl = server.url(URL);
            String result = Commom.httpGet(URL,null,mMockContext);
            assertEquals(ERROR_CONNECTION,result);

        }catch (Exception e){
            e.printStackTrace();
        }



    }

```

==ModelTest.java==
```
 @Test
    public void test_model_Field(){
        Field field = new Field();
        final String ID = "id";
        final String TYPE = "type";

        field.setId(ID);
        field.setType(TYPE);

        assertEquals(ID,field.getId());
        assertEquals(TYPE,field.getType());
    }

    @Test
    public void test_model_Links(){
        final String NEXT = "NEXT";
        final String START = "START";

        Links links = new Links();
        links.setNext(NEXT);
        links.setStart(START);

        assertEquals(NEXT,links.getNext());
        assertEquals(START,links.getStart());
    }
```

# Finally
## About myself

**I am a rich experience android developer who have almost 9 years working experience. I have developed android applications with JAVA from 2010-2015. At that time I learned how to make android application and the basic testing.**

**Then I go to smart phone manufactories as an android system developer form 2016-2018. During this time I maintained the android system secure module , coded for android secure application in system, developed new permission features from new requestments, and resolved all teh permission prolems encountering in CTS and CTA. I also coded for pre-production testing sofeware which is used for testing android mobile phone pre-production in factories**

**Now I am a react-native developer developing application with JavaStript in our country form 2018 to present.**

**I am a well skill android application developer, familiar with android system structure,system source codes. I also  familiar with the core thought of Android testing.**

**Normally I use JAVA, C,C++, JavaScript. Sometimes I use Python to create Android test application. I love coding. My dream is to be a full stack engineer.**
