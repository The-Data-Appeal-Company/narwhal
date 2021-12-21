# Narwhal

![narwhal](https://github.com/The-Data-Appeal-Company/narwhal/raw/master/resources/images/narwhal_128.png)



[![Tests](https://github.com/The-Data-Appeal-Company/narwhal/actions/workflows/ci-tests.yml/badge.svg)](https://github.com/The-Data-Appeal-Company/narwhal/actions/workflows/ci-tests.yml)
![GitHub](https://img.shields.io/github/license/The-Data-Appeal-Company/narwhal)


Iceberg tables maintenance helper 

## Configuration

```yaml
hive:
  name: hive
  uris: thrift://localhost:9083
  warehouse: /tmp
  access_key: ...
  secret_key: ...

rewrite_files:
  enabled: true
  policy:
    type: FILE_SKEW
    config: 
      target_file_size_bytes: 536870912
      file_size_skew_threshold: 0.2

  integration:
    type: SQS
    config:
      access_key: ...
      secret_key: ...
      region: eu-west-1
      queue: https://sqs.eu-west-1.amazonaws.com/<account-id>/<queue>
      batch_size: 10
```

Icon made by [Freepik](https://www.freepik.com) from [flaticon](https://www.flaticon.com)