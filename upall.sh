
cd nodes/bank_node
xterm -T "Bank's node" -e java -jar corda.jar&

cd ../
cd buyer_node
xterm -T "House Buyer's node" -e java -jar corda.jar&

cd ../
cd seller_node
xterm -T "House Seller's node" -e java -jar corda.jar&

cd ../
cd notary_node
xterm -T "Notary's node" -e java -jar corda.jar&

