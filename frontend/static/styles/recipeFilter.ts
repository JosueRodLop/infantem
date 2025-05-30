import { StyleSheet } from "react-native";

module.exports = StyleSheet.create({
  outerContainer: {
    backgroundColor: "white",
    borderRadius: 8,
    overflow: "hidden",
    marginBottom: 16,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
    marginHorizontal: "5%",
    marginVertical: 20,
  },
  animatedContainer: {
    overflow: "hidden",
  },
  toggleButton: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    padding: 16,
    backgroundColor: "#1565C0",
  },
  toggleButtonText: {
    fontSize: 16,
    fontWeight: "bold",
    color: "white",
  },
  expandIcon: {
    color: "white",
    fontSize: 16,
  },
  badge: {
    backgroundColor: "white",
    borderRadius: 10,
    width: 20,
    height: 20,
    justifyContent: "center",
    alignItems: "center",
    marginLeft: 8,
  },
  badgeText: {
    color: "#1565C0",
    fontSize: 12,
    fontWeight: "bold",
  },
  container: {
    padding: 16,
    backgroundColor: "white",
  },
  title: {
    fontSize: 20,
    fontWeight: "bold",
    marginBottom: 16,
    color: "#1565C0",
  },
  inputContainer: {
    marginBottom: 16,
  },
  rowContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 16,
  },
  halfInputContainer: {
    width: "48%",
  },
  label: {
    fontSize: 16,
    marginBottom: 8,
    color: "#1565C0",
    fontWeight: "500",
  },
  input: {
    height: 40,
    borderWidth: 1,
    borderColor: "#D0D0D0",
    borderRadius: 8,
    paddingHorizontal: 12,
    backgroundColor: "white",
    color: "#333333",
  },
  addItemContainer: {
    flexDirection: "row",
    marginBottom: 8,
  },
  addItemInput: {
    flex: 1,
    height: 40,
    borderWidth: 1,
    borderColor: "#D0D0D0",
    borderRadius: 8,
    paddingHorizontal: 12,
    backgroundColor: "white",
    marginRight: 8,
    color: "#333333",
  },
  addButton: {
    backgroundColor: "#1565C0",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
    paddingHorizontal: 16,
  },
  addButtonText: {
    color: "white",
    fontWeight: "500",
  },
  tagsContainer: {
    flexDirection: "row",
    flexWrap: "wrap",
  },
  tag: {
    backgroundColor: "#e6f0ff",
    borderRadius: 16,
    paddingVertical: 6,
    paddingHorizontal: 12,
    margin: 4,
    borderWidth: 1,
    borderColor: "#0052cc",
  },
  tagText: {
    color: "#1565C0",
  },
  buttonContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 16,
    marginBottom: 8,
  },
  resetButton: {
    width: "30%",
    height: 44,
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#1565C0",
  },
  resetButtonText: {
    color: "#1565C0",
    fontWeight: "600",
  },
  applyButton: {
    width: "65%",
    height: 44,
    backgroundColor: "#1565C0",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  applyButtonText: {
    color: "white",
    fontWeight: "600",
  },
});
