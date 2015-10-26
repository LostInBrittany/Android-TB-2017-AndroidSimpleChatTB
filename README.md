## Simple Tchat

This project is a teaching support for an introductory course to Android
development.

The tutorial relies on the use of the [Git](http://git-scm.com/) versioning system for source code management. You don't need to know anything about Git to follow the tutorial other than how to install and run a few git commands.


### Step-00: Create project

You begin a new project, `SimpleTchat`.

![New project](./img/001.png)

You choose to begin with an empty activity.

![Add empty activity](./img/002.png)

You call the activity `SignupActivity`.

![Customize activity](./img/003.png)

And you have the new project in the IDE.

![Project in Android Studio](./img/004.png)


### Step-01: Create the activities

Reset the workspace to step-1.

```
git checkout -f step-01
```

In this step you're going to create the three main Activities for the SimpleTchat:
`SignupActivity`, `SigninActivity` and `MessageActivity`:

![Activities](./img/005.png)

For each Actitity you create the UI widgets, you give them unique ID and you
externalize all the strings.

![SignupActivity](./img/004.png)
