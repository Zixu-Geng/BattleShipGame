## Task 6. Placeholder BasicShip class

Our real BasicShip is going to have a fairly good bit of functionality,
but we aren't going to write it all at once.  Instead, we are going to make
a BasicShip that just occupies one square.  Also, our real
BasicShip will be abstract, and this placeholder will not.

Start out by making BasicShip.java, creating a skeleton, and making
it implement Ship<Character>:
```java
public class BasicShip implements Ship<Character>
```
You will notice that when you do that, BasicShip gets a red underline:
Ship has promised methods we have not implemented yet.  If you do
"C-c C-a" Emacs will automatically generate all the methods you are missing.
You should now have a bunch of methods with a
```java
		// TODO Auto-generated method stub
```
comment in them.  This gets a green underline and a green ! in the margin

For this placeholder BasicShip, we aren't even going to change most of these.
We are going to add a private final Coordinate myLocation, and
a constructor that takes a Coordinate and initalizes where with it.
Then we are going to make occupiesCoordinates only return
```java
  where.equals(myLocation)
```
and we are going to make getDisplayInfoAt just always return 's'.

We note that this placeholder class does not really conform to the
specifications, and we aren't going to write tests for it.
While this may seem shocking, we are just circumventing the fact
that you all have not learned Mocking yet. We just want this class
to help us test in Tasks 7-9.  We'll replace it completely in
Task 10.


You finished step 6.

***

>[!IMPORTANT]
> - Add all the files to source control (GitHub) using `git add -A`
> - Commit and Push 

>[!NOTE]
> - You can now proceed to [Task 7](./task7.md)