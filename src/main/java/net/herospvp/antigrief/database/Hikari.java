package net.herospvp.antigrief.database;

import net.herospvp.antigrief.Conf;
import net.herospvp.antigrief.Main;
import net.herospvp.antigrief.Utils;
import net.herospvp.heroscore.HerosCore;
import net.herospvp.heroscore.utils.builders.Message;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Hikari {

    private static final Message.Builder builder = Main.getBuilder();

    public static void initializeDatabase() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = HerosCore.getInstance().getConnectionPoolManager().getConnection();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + Conf.databaseTable +
                    " (username CHAR(16) NOT NULL, pin CHAR(6) NOT NULL, position CHAR(16) NOT NULL, ip CHAR(46), PRIMARY KEY(username));");
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("SELECT * FROM " + Conf.databaseTable);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String playerName = resultSet.getString(1), pin = resultSet.getString(2),
                        rank = resultSet.getString(3), ip = resultSet.getString(4);

                Store.setEveryThing(playerName, rank, pin, ip);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            HerosCore.getInstance().getConnectionPoolManager().close(connection, preparedStatement, resultSet);
        }
    }

    public static void getInfo(CommandSender commandSender, String playerName) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = HerosCore.getInstance().getConnectionPoolManager().getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + Conf.databaseTable
                    + " WHERE username = " + Utils.format(playerName));
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String resultName = resultSet.getString(1), resultRank = resultSet.getString(3),
                        resultHostName = resultSet.getString(4);
                List<String> strings = Arrays.asList("Nome: ", "\nPin: *****", "\nRank: ", "\nIP: ");

                List<String> result = Utils.checkIfIsPlayer(commandSender, "antigrief-ag-2", strings);
                int i = 0;
                for (String string : result) {
                    switch (i) {
                        case 0: builder.sendMessage(commandSender, string + resultName); break;
                        case 1: builder.sendMessage(commandSender, string + "*****"); break;
                        case 2: builder.sendMessage(commandSender, string + resultRank); break;
                        case 3: builder.sendMessage(commandSender, string + resultHostName); break;
                    }
                    i++;
                }
            } else {
                String result = Utils.checkIfIsPlayer(commandSender, "antigrief-ag-1", "Non esistono records di ");
                builder.sendMessage(commandSender, result + playerName, Message.Builder.Result.ERR);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } finally {
            HerosCore.getInstance().getConnectionPoolManager().close(connection, preparedStatement, resultSet);
        }
    }

    public static void executeUpdate(String username, String statement) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HerosCore.getInstance().getConnectionPoolManager().getConnection();
            preparedStatement = connection.prepareStatement(statement + Utils.format(username));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            HerosCore.getInstance().getConnectionPoolManager().close(connection, preparedStatement, null);
        }
    }

}
