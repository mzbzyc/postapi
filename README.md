# postapi

获取所有POST

```sh
GET		http://127.0.0.1:9000/posts
```

结果

```json
[
  {
    "categories": [
      "周杰伦"
    ],
    "content": "阿萨德飒沓大苏打",
    "post_uuid": "aa7ee367-cbdd-4a5a-9a4e-c9a7e0d736aa",
    "publishing_date": 1439308800000,
    "title": "周杰伦梦想导师"
  }
]
```

发布一篇POST

![](http://i.imgur.com/ZzY1FIC.png)

```sh
POST	http://127.0.0.1:9000/post/save
```

请求body

```json
{
    "title": "周杰伦梦想导师", 
    "categories": [
        "周杰伦"
    ], 
    "content": "阿萨德飒沓大苏打"
}
```


