# DHT Protocol


## Responsibility

A node is responsible for hash if:
  node = node.last
  or
  hash(node.last) <= hash < hash(node)

## Create Node

Send:
{:add-node
  {:ip ip}}

The node responsible for hash(ip) will:
  Send to last:
    {:set-pointers
      {:next ip}}
  Send to ip:
    {:update-pointers
      {:last last
       :next self}
     :transfer-keys
      {:map keys to be transfered to new node}}
  Remove keys from map that were moved to new node.

## Get value

If not responsible:
  Send:
    {:get-value
      {:key key
       :return-to where to return value}}

If responsible:
  Send to ?:
    {:return-value
      {:value value
       :return-to where to return value}}

## Put value

If not responsible:
  Send:
    {:put-value
      {:key key
       :value value}}

If responsible:
  Add key value pair




