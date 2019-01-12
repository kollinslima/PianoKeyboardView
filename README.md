# PianoKeyboardView

A simple keyboard view based on [Sylvain Saurel's implementation](https://medium.com/@ssaurel/creating-a-virtual-piano-for-android-b6d3ac05d961)

__This is not a piano app__, it's just a piano layout to be used in your project.

## How to get it?

To use PianoKeyboardView, follow these steps:

1. Download or clone the project's repository;
2. In your project, go to __File -> New -> Import Module...__
3. Select the source directory (_pianokeyboardview_) and click __Finish__
4. Go to __File -> Project Structure...__ and change to __Dependencies__ tab in your app Module
5. Click the __+__ button, choose __Module dependency__ and select the pianokeyboard module
6. Just click __Ok__ and you are good to go.

## Usage

### XML
>```xml
><com.kollins.pianokeyboardview.PianoKeyboardView
>        android:id="@+id/pianoTest"
>        android:layout_width="match_parent"
>        android:layout_height="match_parent"
>        app:numKeys="14"
>        app:midiStart="60"
>        app:blackKeyColor="#FF0000"
>        app:whiteKeyColor="#000000"
>        app:pressedColor="#FFFFFF"/>
>```

### Java
>```java
> PianoKeyboardView piano;
> piano = (PianoKeyboardView) findViewById(R.id.pianoTest);
>        
> piano.setKeyEventListener(new OnKeyChangeListener() {
>     @Override
>     public void onKeyPressed(int midiNote) {
>       Log.v("PIANO", "Key Pressed: " + midiNote);
>     }
>
>     @Override
>     public void onKeyReleased(int midiNote) {
>       Log.v("PIANO", "Key Released: " + midiNote);
>      }
> });
>```
