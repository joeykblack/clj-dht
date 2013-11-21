# DHT Protocol


## Responsibility

A node is responsible for hash if:
  hash(node.last) <= hash < hash(node)

## Create Node

Send:
{:type add-node
  :ip ip}

The node responsible for hash(ip) will:
  Send to last:
    {:type set-pointers
      :next ip}
  Send to ip:
    {:type update-pointers
      :last last
      :next self}
    {:type transfer-keys
      :map keys to be transfered to new node}
  Remove keys from map that were moved to new node.

## Get value

If not responsible:
  Send:
    {:type get-value
      :key key
      :return-to where to return value}

If responsible:
  Send to ?:
    {:type return-value
      :value value
      :return-to where to return value}

## Put value

If not responsible:
  Send:
    {:type put-value
      :key key
      :value value}

If responsible:
  Add key value pair
