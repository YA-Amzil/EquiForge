package be.pxl.researchproject.scheduler;

public enum DewormingMedication {
    PANACUR("Panacur"),
    PYRANTEL("Pyrantel"),
    ERAQUELL_FUREXCEL("Eraquell or Furexcel");

    private final String name;

    DewormingMedication(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
