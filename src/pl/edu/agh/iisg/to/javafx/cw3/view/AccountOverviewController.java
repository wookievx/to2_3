package pl.edu.agh.iisg.to.javafx.cw3.view;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.edu.agh.iisg.to.javafx.cw3.command.Command;
import pl.edu.agh.iisg.to.javafx.cw3.command.CommandRegistry;
import pl.edu.agh.iisg.to.javafx.cw3.command.GenericCommand;
import pl.edu.agh.iisg.to.javafx.cw3.model.Account;
import pl.edu.agh.iisg.to.javafx.cw3.model.Category;
import pl.edu.agh.iisg.to.javafx.cw3.model.Transaction;
import pl.edu.agh.iisg.to.javafx.cw3.presenter.AccountPresenter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AccountOverviewController {

	private Account data;

	private AccountPresenter presenter;

	private CommandRegistry commandRegistry;

	@FXML
	private TableView<Transaction> transactionsTable;

	@FXML
	private TableColumn<Transaction, LocalDate> dateColumn;

	@FXML
	private TableColumn<Transaction, String> payeeColumn;

	@FXML
	private TableColumn<Transaction, Category> categoryColumn;

	@FXML
	private TableColumn<Transaction, String> memoColumn;

	@FXML
	private TableColumn<Transaction, BigDecimal> outflowColumn;

	@FXML
	private TableColumn<Transaction, BigDecimal> inflowColumn;

	@FXML
	private TableColumn<Transaction, BigDecimal> balanceColumn;

	@FXML
	private ListView<Command> commandLogView;

	@FXML
	private Button deleteButton;

	@FXML
	private Button editButton;

	@FXML
	private Button addButton;

	@FXML
	private Button undoButton;

	@FXML
	private Button redoButton;

	@FXML
	private void initialize() {
		transactionsTable.getSelectionModel().setSelectionMode(
				SelectionMode.MULTIPLE);

		dateColumn.setCellValueFactory(dataValue -> dataValue.getValue()
				.getDateProperty());
		payeeColumn.setCellValueFactory(dataValue -> dataValue.getValue()
				.getPayeeProperty());
		categoryColumn.setCellValueFactory(dataValue -> dataValue.getValue()
				.getCategoryProperty());
		inflowColumn.setCellValueFactory(dataValue -> dataValue.getValue()
				.getInflowProperty());
		deleteButton.disableProperty().bind(
				Bindings.isEmpty(transactionsTable.getSelectionModel()
						.getSelectedItems()));

		editButton.disableProperty().bind(
				Bindings.size(
						transactionsTable.getSelectionModel()
								.getSelectedItems()).isNotEqualTo(1));
	}

	@FXML
	private void handleDeleteAction(ActionEvent event) {
        final List<Transaction> selectedTransactions = transactionsTable
                .getSelectionModel()
                .getSelectedItems()
                .stream()
                .collect(Collectors.toList());

        Runnable actionDo = () -> selectedTransactions.forEach(data::removeTransaction);
        Runnable actionUndo = () -> selectedTransactions.forEach(data::addTransaction);
        Command c = new GenericCommand(actionDo, actionUndo);
        commandRegistry.executeCommand(c);
	}

	@FXML
	private void handleEditAction(ActionEvent event) {
		Transaction transaction = transactionsTable.getSelectionModel()
				.getSelectedItem();
		if (transaction != null) {
			presenter.showTransactionEditDialog(transaction);
		}
	}

	@FXML
	private void handleAddAction(ActionEvent event) {
		final Transaction transaction = Transaction.newTransaction();

        Command c = new GenericCommand(() -> data.addTransaction(transaction), () -> data.removeTransaction(transaction));

		if (presenter.showTransactionEditDialog(transaction)) {
			commandRegistry.executeCommand(c);
		}
	}

	@FXML
	private void handleUndoAction(ActionEvent event) {
		commandRegistry.undo();
	}

	@FXML
	private void handleRedoAction(ActionEvent event) {
		commandRegistry.redo();
	}

	public void setData(Account acccount) {
		this.data = acccount;
		transactionsTable.setItems(data.getTransactions());
	}

	public void setPresenter(AccountPresenter presenter) {
		this.presenter = presenter;
	}

	public void setCommandRegistry(CommandRegistry commandRegistry) {
		this.commandRegistry = commandRegistry;
		commandLogView.setItems(commandRegistry.getCommandStack());
		commandLogView.setCellFactory(lv -> new ListCell<Command>() {
			protected void updateItem(Command item, boolean empty) {
				super.updateItem(item, empty);
				setText((item != null && !empty) ? item.getName() : null);
			};
		});
	}
}
