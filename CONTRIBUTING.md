## Contribution policy

**Table of contents:**

- [General guidelines](#general-guidelines)
- [Commit message formats](#commit-message-formats)
- [Gitlab issues](#gitlab-issues)
- [Content](#content)

### General guidelines

**Never push to master**. Small development contributions or general, widely applicable development subproject (e.g. chess piece classes, which are used by almost all other code) may be pushed directly to the `develop` branch. Larger features which are not essential to all of the project should have their own branch. Examples: Branches `ui` or `ai`.

**Do not push non-building commits**. You should always try to build your project before pushing the code. Nevertheless, once in a while we all screw up. Try to fix mistakes like these as soon as they occur. Anyone noticing a commit which does not build may apply a fix. The fix does not necessarily need to implement the correct behavior of the code which makes the build fail, but it should ensure that the project compiles.

**Only commit meaningful changes**. Avoid commits where the only difference is added trailing whitespace in a single file, and other such things. As a general rule of thumb, if you cannot describe the reason that your change should be committed, do not create a commit for it.

### Commit message formats

Use:

- `[<affected files>] <change description>` for **small commits** affecting at most one or two files
  - Example: `[contribution-policy.md] added`
  - Refer to files in a recognizable manner
- `[#<issue number>] <change description>` for changes made to **advance progress of a specific issue**
  - Example: `[#9001] added support for higher power levels`
- `[<area of project>] <change description>` for commits **affecting multiple files**, where there is no obvious issue for the change to refer to
  - Example: `[Chess pieces] remove NullPointerException bug`
  - Example: `[AI] throw exception if AI attempting to become Skynet`
- `[<short hash of non-building commit>] fix(ed)` for commits created to **fix a non-building commit**
  - Example: `[575b7ff] fixed`

Additional details about the commit may be provided in the commit message body, but only message titles are subject to this style guide, and message bodies may be omitted.

### Gitlab issues

**Include a description of the change to be made**. The more specifics you can include, the easier it is to complete the task. Suggestions of approaches or references to related information is appreciated.

**Aggregate related small changes**. For instance, when creating an issue for extending an API, also include the subclasses which need to implement the additional method(s) in order for the project to compile.

**Refer to related issues in your commit messages**. This makes it easier to see which commits are connected to which parts of the project. You can refer to an issue using the syntax `#<issue number>` (e.g. `#42`) anywhere in your commit message.

**Don't be afraid to create an issue**. If you see something in the project that you want done but you can't work on right now, create an issue for it.

**Label your issues**. All issues should be labeled with at least one label indicating what category it belongs to. Examples of broadly applicable labels includes `To Do` and `Doing`, but you may also use something more specific, such as `Bug`, `Tests`, etc.

**Unassigned issues are free game**. If you don't have anything to do, you may claim any issue which does not yet have an assignee. If you want to work on an issue someone else is working on, leave a heads-up in our Discord communication channel.

### Content

While we do not enforce very strict coding style guidelines, we encourage all team members to follow best practices, and to write code with readability and maintainability in mind. Names of classes, methods, fields and comments should be written in English.

[Return to top](#contribution-policy)
