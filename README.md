# instagram-clone
Instagram clone app for android

# Content
- Features
- Installation
- Implementations
- Permissions
- Api Docs
- Database Design
- Screenshots

# Features
- MVVM
- Data Binding
- DI (Hilt-Dagger2)
- Shared Preferences
- Retrofit
- Rest Api (PHP-MySQL)

# Installation
Installation steps:
- **clone repository:** <br/> `git clone https://github.com/serhat-demir/instagram-clone.git`
- **open project on android studio ide:** <br/> `file > open`
- **change base url in api class:** <br/> `com.serhat.instagram.api.ApiUtils`
- **create database:** <br/> `create database instagram_clone`
- **import tables into your database:** <br/> `api/v1/utils/instagram_clone.sql`
- **clone api files to your server:** <br/> `git clone https://github.com/serhat-demir/instagram-clone-api.git`
- **change database host, name, user and password with yours:** <br/> `api/v1/utils/db.php`

# Implementations
- ViewModel-LiveData
- Hilt-Dagger2
- Retrofit
- Gson
- Navigation
- Picasso
- Swipe Refresh Layout

# Permissions
Required permissions to run this application:
- Internet
- Read External Storage
- Write External Storage

# Api Docs
- **Base Url** -> http://your_site_adress/api/v1/
- **Authentication** -> Basic Auth
- **Authentication User** -> admin
- **Authentication Password** -> 123456

You can change auth user and password,<br/>
**in api** ->`api/v1/utils/db.php`<br/>
**in application** ->`com.serhat.instagram.api.ApiClient`

## Api Endpoints
### users.php/
| Request Method | Parameters | Response | Description |
| :------------: | :------------: | :------------: | :------------: |
| GET |  | User List | Get all users |
| GET | user_name (string) | User List | Filter users by name |
| GET | user_id (integer) | User | Get user details |
| POST | user_name (string) <br/> user_password (string) | User | Sign In |
| POST | user_email (string) <br/> user_name (string) <br/>user_password (string) | User | Sign Up |
| POST | user_id (integer) <br/> image (file) | User | Update profile photo |
| POST | user_id (integer) <br/> image_name (string) | User | Remove profile photo |
| PUT | user model | User | Update user |

### posts.php/
| Request Method | Parameters | Response | Description |
| :------------: | :------------: | :------------: | :------------: |
| GET | user_id (integer) <br/> is_feed = 1 | Post List | Get feed |
| GET | user_id (integer) <br/> is_feed = 0 | Post List | Get posts (in profile page) |
| GET | post_id (integer) | Post | Get post details |
| POST | image (file) <br/> post_description (string) <br/> post_owner (integer) | Api Response | Share post |
| PUT | post model | Api Response | Edit post |
| DELETE | post_id (integer) | Api Response | Delete post |

### comments.php/
| Request Method | Parameters | Response | Description |
| :------------: | :------------: | :------------: | :------------: |
| POST | comment_text (string) <br/> comment_post (integer) <br/> comment_owner (integer) | Api Response | Share post |
| PUT | comment model | Api Response | Edit comment |
| DELETE | comment_id (integer) | Api Response | Delete comment |

### notifications.php/
| Request Method | Parameters | Response | Description |
| :------------: | :------------: | :------------: | :------------: |
| GET | user_id (integer) | Notification List | Get notifications |
| PUT | user model | Api Response | Mark notifications as seen |
| DELETE | user_id (integer) | Api Response | Clear notifications |

### saved_posts.php/
| Request Method | Parameters | Response | Description |
| :------------: | :------------: | :------------: | :------------: |
| GET | user_id (integer) | Post List | Get saved posts |
| POST | user_id (integer) <br/> post_id (integer) | Api Response | Save post |
| DELETE | user_id (integer) <br/> post_id (integer) | Api Response | Unsave post |

### post_likes.php/
| Request Method | Parameters | Response | Description |
| :------------: | :------------: | :------------: | :------------: |
| POST | user_id (integer) <br/> post_id (integer) | Api Response | Like post |
| DELETE | user_id (integer) <br/> post_id (integer) | Api Response | Unlike post |

### follow.php/
| Request Method | Parameters | Response | Description |
| :------------: | :------------: | :------------: | :------------: |
| POST | follower_id (integer) <br/> following_id (integer) | Api Response | Follow |
| DELETE | follower_id (integer) <br/> following_id (integer) | Api Response | Unfollow |

# Database Design
### users
| column | type |
| :------------ | :------------ |
| user_id (pk) | int(11) |
| user_email | varchar(50) |
| user_name | varchar(25) |
| user_password | varchar(50) |
| user_fullname | varchar(50) |
| user_photo | varchar(15) |
| user_bio | varchar(250) |
| user_profile_private | tinyint(1) |

### notifications
| column | type |
| :------------ | :------------ |
| notification_id (pk) | int(11) |
| notification_text | text |
| notification_resource (fk: users, posts) | int(11) |
| notification_type | int(11) |
| notification_receiver (fk: users) | int(11) |
| received_at | varchar(24) |
| is_seen | tinyint(1) |

### posts
| column | type |
| :------------ | :------------ |
| post_id (pk) | int(11) |
| post_photo | varchar(15) |
| post_description | varchar(250) |
| post_owner (fk: users) | int(11) |
| created_at | varchar(24) |

### comments
| column | type |
| :------------ | :------------ |
| comment_id (pk) | int(11) |
| comment_text | varchar(250) |
| comment_post (fk: posts) | int(11) |
| comment_owner (fk: users) | int(11) |
| created_at | varchar(24) |

### follow
| column | type |
| :------------ | :------------ |
| follower_id (fk: users) | int(11) |
| following_id (fk: users) | int(11) |

### post_likes
| column | type |
| :------------ | :------------ |
| post_id (fk: posts) | int(11) |
| user_id (fk: users) | int(11) |

### saved_posts
| column | type |
| :------------ | :------------ |
| post_id (fk: posts) | int(11) |
| user_id (fk: users) | int(11) |

## triggers
| table | time | event | description |
| :------------ | :------------ | :------------ | :------------ |
| posts | after | delete | delete comments, post likes etc. |
| follow | after | insert | send notification |
| post_likes | after | insert | send notification |
| comments | after | insert | send notification |

# Screenshots
![img1](https://img001.prntscr.com/file/img001/wT4tZK8-QBCzDvPATL8zjQ.jpg "img1")

![img2](https://img001.prntscr.com/file/img001/h14QUkAUSyuNyg0qdx8tzA.jpg "img2")

![img3](https://img001.prntscr.com/file/img001/DwogW2vLT_if0RQH8nTcOg.jpg "img3")
