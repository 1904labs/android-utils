# Changelog
All notable changes to this module will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0]
### Added
- `BaseBiometricsHelper`
    - Base class that creates ciphers, keys, and IVs, and also handles encryption, decryption, storage, and retrieval of the data. This class
    also has basic logging and error handling as well as some helper functions to help keep the UI logic clean and readable.
- `CredentialBioHelper`
    - Implementation of ```BaseBiometricHelper``` that works with a username and password.
- `TokenBioHelper`
    - Implementation of ```BaseBiometricHelper``` that works with a string based token.