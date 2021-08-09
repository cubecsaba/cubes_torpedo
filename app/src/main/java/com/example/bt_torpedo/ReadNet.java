package com.example.bt_torpedo;

//import static com.example.bt_torpedo.Net_Connect.input;


public class ReadNet extends Thread {

    private String user_i;


    public void run() {
      /*  while (true) {
            try {
                final String message = input.readLine();
                //
                if (message != null && message.startsWith("ULIST:")) { //notification about new user's logon
                    user_i = message.substring(6);
                    usersList.add(user_i); // update users' list
                    arrayAdapter.notifyDataSetChanged(); //update lvUsers
                    tvMessage.setText("Select the user, you want to play with:");

                } else if (message != null && message.startsWith("DelULIST:")) { // notification about logoff of a user
                    user_i = message.substring(9);
                    usersList.remove(user_i); //remove user from the users' list
                    arrayAdapter.notifyDataSetChanged(); //update lvUsers

                } else if (message != null && message.startsWith("ALONE:")) { // in case no other user logged on on the server
                    tvMessage.setText("Currently no other user logged on.");

                } else if (message != null && message.startsWith("PARTNER:")) { // in case no other user logged on on the server
                    String[] piecesOfMessage = message.split(":");
                    etPartner.setText(piecesOfMessage[1]);
                    // tag User`s list item, so that player see who invited him to play;

                } else {
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvMessages.append(message + "\n");
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    */}

}
