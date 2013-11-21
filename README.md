# dht 0.0.1

by [Joey K Black](joey-black.appspot.com)

This is an academic project to explore some of the technical challenges of creating a network on top of current infrastructure that is:
* Secure
* Anonymous
* Decentralized

This project is for information purposes only. It is not intended to address any moral or legal issues. I do not take responsibility for the use or misuse of this code.

## Usage

TBD

## Design

Combine concepts from:
* Tor
* p2p (ex. bittorrent)
* Cloud hosting

Goals:
* Distribute and consume content anonymously
* Provide system for authentication

Distribute steps:
* Create content package with version and identifier
* Sign package
* Publish

Consumption steps:
* Search for identifier of desired content
* Broadcast request for content with specific signer
* Validate result
* Host consumed content

## License

Copyright © 2013

Distributed under the Eclipse Public License, the same as Clojure.

