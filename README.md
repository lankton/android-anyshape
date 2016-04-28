# android-anyshape
**With the solution, pictures can be displayed in any shape on Android platform.**

##Effect##
The left is the UI using normal ImageViews, and the right is UI using the AnyshapeImageViews  
<img src="https://github.com/lankton/android-anyshape/blob/master/pictures/with_normal.jpg" width="200px"/>
<img src="https://github.com/lankton/android-anyshape/blob/master/pictures/with_mask_gif.gif" width="200px"/>  
All we need to make are 3 mask PNGs like below (**the background must be transparent**):   
<img src="https://github.com/lankton/android-anyshape/blob/master/pictures/singerstar_1.png" height="100px"/>
<img src="https://github.com/lankton/android-anyshape/blob/master/pictures/text_1.png" height="100px"/>
<img src="https://github.com/lankton/android-anyshape/blob/master/pictures/rings_1.png" height="100px"/>

##Usage##
You can use the AnyshapeImageView like below in your layout file:

```xml
<cn.lankton.anyshape.AnyshapeImageView
    android:layout_width="150dp"
    android:layout_height="150dp"
    android:layout_marginTop="20dp"
    android:src="@drawable/kumamon"
    app:anyshapeMask="@drawable/singlestar"/>
```
You can use the attribue "anyshapeMask" to set the mask. AnyshapeImageView can get the path from the mask, by detecting the alpha value of every pixel.

if you want to create a view with pure color , you can use the code like this:  

```xml
<cn.lankton.anyshape.AnyshapeImageView
    android:layout_width="210dp"
    android:layout_height="140dp"
    android:layout_marginTop="20dp"
    app:anyshapeBackColor="@android:color/holo_red_light"
    app:anyshapeMask="@drawable/rings"/>
```
If you want to change the pure color **dynamically**, do like this:

```java
anyshapeImageView.setBackColor(Color.Green);
```
If your AnyshapeImageView is too big to show shaped image quickly, you may need to init the path before its being used. 
**In most situations, you don't need to do this. The usage in layout file above would be enough.**
```java
List<Integer> ids = new ArrayList<>();  
ids.add(R.drawable.singlestar);  
ids.add(R.drawable.rings);
ids.add(R.drawable.text);
PathManager.getInstance().createPaths(this, ids);
```
update: 2016-04-27
add a new method to init paths using self-difined limit for the mask bitmap, if you know the size of your AnyshapeImageView.
```java
// the unit of the limit is px.
PathManager.getInstance().createPaths(this, ids, 1000, 1000);
```

##summary##
The code is not hard for anybody to understand. Just hope that it can provide some help or inspiration for you.

