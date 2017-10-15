# AndroidCrashDummy
Exceptions, logs, more playing about (@julianharty)

## What?
This app is for testing exceptions and logging. It allows us to implement and test approaches to improving logging/testing.

## Testing Logging
In this app, there is an instrumentation test called "clickLog100Button".
This test clicks a UI button which triggers 100 log messages to be created. 
The test then looks for a specific final message (using RegExp) and passes/fails based on it's presence.

```java
@Test
public void clickLog100Button() throws Exception {

    clearLog(); // Otherwise our logcat command could show previous test-runs etc.
    onView(withId(R.id.log100)).perform(click()); // UI Action / Trigger logs
    String log = getLogs(); // Get all logs in logcat buffer for this app (could have logs from parallel executions, not just the line above)

    Log.d("LogTest", "Output from log reading has length: [" + log.length() + "]");

    Pattern mPattern = Pattern.compile("Logging 100 messages took [0-9]+ Milliseconds");

    Matcher matcher = mPattern.matcher(log);
    if(!matcher.find()) {
        throw new Exception("Logging was not detected!");
    } else {
        Log.i("LogTest", "ClickLog100Button Passed");
    }
}
```
