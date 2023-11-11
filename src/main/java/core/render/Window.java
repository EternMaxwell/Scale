package core.render;

import org.lwjgl.glfw.*;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;
    private int width;
    private int height;
    private String title;
    private int xpos, ypos;

    private IntValue resizable, visible, decorated, focused, autoIconify, floating, maximized, centerCursor, transparentFramebuffer, focusOnShow, scaleToMonitor, mousePassThrough;
    private IntValue redBits, greenBits, blueBits, alphaBits, depthBits, stencilBits, accumRedBits, accumGreenBits, accumBlueBits, accumAlphaBits, auxBuffers, samples, refreshRate;
    private IntValue stereo, srgbCapable, doublebuffer, clientApi, contextCreationApi, contextVersionMajor, contextVersionMinor, contextRobustness, contextReleaseBehavior, contextNoError, openglForwardCompat, openglDebugContext, openglProfile;
    private IntValue win32KeyboardMenu, cocoaRetinaFramebuffer, cocoaGraphicsSwitching;
    private String cocoaFrameName, x11ClassName, x11InstanceName;

    public static class IntValue{
        public int value;
    }

    /**
     * whether the window is resizable
     * @param resizable whether the window is resizable. One of:
     *                  <p>{@link org.lwjgl.glfw.GLFW#GLFW_TRUE} if the window is resizable</p>
     *                  <p>{@link GLFW#GLFW_FALSE} if the window is not resizable</p>
     *                  <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setResizable(boolean resizable) {
        if(this.resizable == null)
            this.resizable = new IntValue();
        this.resizable.value = resizable ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the window is visible
     * @param visible whether the window is visible. One of:
     *                <p>{@link GLFW#GLFW_TRUE} if the window is visible</p>
     *                <p>{@link GLFW#GLFW_FALSE} if the window is not visible</p>
     *                <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setVisible(boolean visible) {
        if(this.visible == null)
            this.visible = new IntValue();
        this.visible.value = visible ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the window is decorated
     * @param decorated whether the window is decorated. One of:
     *                  <p>{@link GLFW#GLFW_TRUE} if the window is decorated</p>
     *                  <p>{@link GLFW#GLFW_FALSE} if the window is not decorated</p>
     *                  <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setDecorated(boolean decorated) {
        if(this.decorated == null)
            this.decorated = new IntValue();
        this.decorated.value = decorated ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the window is focused
     * @param focused whether the window is focused. One of:
     *                <p>{@link GLFW#GLFW_TRUE} if the window is focused</p>
     *                <p>{@link GLFW#GLFW_FALSE} if the window is not focused</p>
     *                <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setFocused(boolean focused) {
        if(this.focused == null)
            this.focused = new IntValue();
        this.focused.value = focused ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the window is auto iconified
     * @param autoIconify whether the window is auto iconified. One of:
     *                    <p>{@link GLFW#GLFW_TRUE} if the window is auto iconified</p>
     *                    <p>{@link GLFW#GLFW_FALSE} if the window is not auto iconified</p>
     *                    <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setAutoIconify(boolean autoIconify) {
        if(this.autoIconify == null)
            this.autoIconify = new IntValue();
        this.autoIconify.value = autoIconify ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the window is floating
     * @param floating whether the window is floating. One of:
     *                 <p>{@link GLFW#GLFW_TRUE} if the window is floating</p>
     *                 <p>{@link GLFW#GLFW_FALSE} if the window is not floating</p>
     *                 <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setFloating(boolean floating) {
        if(this.floating == null)
            this.floating = new IntValue();
        this.floating.value = floating ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the window is maximized
     * @param maximized whether the window is maximized. One of:
     *                  <p>{@link GLFW#GLFW_TRUE} if the window is maximized</p>
     *                  <p>{@link GLFW#GLFW_FALSE} if the window is not maximized</p>
     *                  <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setMaximized(boolean maximized) {
        if(this.maximized == null)
            this.maximized = new IntValue();
        this.maximized.value = maximized ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the cursor is centered
     * @param centerCursor whether the cursor is centered. One of:
     *                     <p>{@link GLFW#GLFW_TRUE} if the cursor is centered</p>
     *                     <p>{@link GLFW#GLFW_FALSE} if the cursor is not centered</p>
     *                     <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setCenterCursor(boolean centerCursor) {
        if (this.centerCursor == null)
            this.centerCursor = new IntValue();
        this.centerCursor.value = centerCursor ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the framebuffer is transparent
     * @param transparentFramebuffer whether the framebuffer is transparent. One of:
     *                               <p>{@link GLFW#GLFW_TRUE} if the framebuffer is transparent</p>
     *                               <p>{@link GLFW#GLFW_FALSE} if the framebuffer is not transparent</p>
     *                               <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setTransparentFramebuffer(boolean transparentFramebuffer) {
        if(this.transparentFramebuffer == null)
            this.transparentFramebuffer = new IntValue();
        this.transparentFramebuffer.value = transparentFramebuffer ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the window is focused on show
     * @param focusOnShow whether the window is focused on show. One of:
     *                    <p>{@link GLFW#GLFW_TRUE} if the window is focused on show</p>
     *                    <p>{@link GLFW#GLFW_FALSE} if the window is not focused on show</p>
     *                    <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setFocusOnShow(boolean focusOnShow) {
        if(this.focusOnShow == null)
            this.focusOnShow = new IntValue();
        this.focusOnShow.value = focusOnShow ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the window is scaled to monitor
     * @param scaleToMonitor whether the window is scaled to monitor. One of:
     *                       <p>{@link GLFW#GLFW_TRUE} if the window is scaled to monitor</p>
     *                       <p>{@link GLFW#GLFW_FALSE} if the window is not scaled to monitor</p>
     *                       <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setScaleToMonitor(boolean scaleToMonitor) {
        if(this.scaleToMonitor == null)
            this.scaleToMonitor = new IntValue();
        this.scaleToMonitor.value = scaleToMonitor ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * whether the mouse is passed through
     * @param mousePassThrough whether the mouse is passed through. One of:
     *                         <p>{@link GLFW#GLFW_TRUE} if the mouse is passed through</p>
     *                         <p>{@link GLFW#GLFW_FALSE} if the mouse is not passed through</p>
     *                         <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setMousePassThrough(boolean mousePassThrough) {
        if(this.mousePassThrough == null)
            this.mousePassThrough = new IntValue();
        this.mousePassThrough.value = mousePassThrough ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the red bits
     * @param redBits the red bits
     *                <p>Default is: 8</p>
     */
    public void setRedBits(int redBits) {
        if(this.redBits == null)
            this.redBits = new IntValue();
        this.redBits.value = redBits;
    }

    /**
     * set the green bits
     * @param greenBits the green bits
     *                  <p>Default is: 8</p>
     */
    public void setGreenBits(int greenBits) {
        if(this.greenBits == null)
            this.greenBits = new IntValue();
        this.greenBits.value = greenBits;
    }

    /**
     * set the blue bits
     * @param blueBits the blue bits
     *                 <p>Default is: 8</p>
     */
    public void setBlueBits(int blueBits) {
        if(this.blueBits == null)
            this.blueBits = new IntValue();
        this.blueBits.value = blueBits;
    }

    /**
     * set the alpha bits
     * @param alphaBits the alpha bits
     *                  <p>Default is: 8</p>
     */
    public void setAlphaBits(int alphaBits) {
        if(this.alphaBits == null)
            this.alphaBits = new IntValue();
        this.alphaBits.value = alphaBits;
    }

    /**
     * set the depth bits
     * @param depthBits the depth bits
     *                  <p>Default is: 24</p>
     */
    public void setDepthBits(int depthBits) {
        if(this.depthBits == null)
            this.depthBits = new IntValue();
        this.depthBits.value = depthBits;
    }

    /**
     * set the stencil bits
     * @param stencilBits the stencil bits
     *                    <p>Default is: 8</p>
     */
    public void setStencilBits(int stencilBits) {
        if(this.stencilBits == null)
            this.stencilBits = new IntValue();
        this.stencilBits.value = stencilBits;
    }

    /**
     * set the accum red bits
     * @param accumRedBits the accum red bits
     *                     <p>Default is: 0</p>
     */
    public void setAccumRedBits(int accumRedBits) {
        if(this.accumRedBits == null)
            this.accumRedBits = new IntValue();
        this.accumRedBits.value = accumRedBits;
    }

    /**
     * set the accum green bits
     * @param accumGreenBits the accum green bits
     *                       <p>Default is: 0</p>
     */
    public void setAccumGreenBits(int accumGreenBits) {
        if(this.accumGreenBits == null)
            this.accumGreenBits = new IntValue();
        this.accumGreenBits.value = accumGreenBits;
    }

    /**
     * set the accum blue bits
     * @param accumBlueBits the accum blue bits
     *                      <p>Default is: 0</p>
     */
    public void setAccumBlueBits(int accumBlueBits) {
        if(this.accumBlueBits == null)
            this.accumBlueBits = new IntValue();
        this.accumBlueBits.value = accumBlueBits;
    }

    /**
     * set the accum alpha bits
     * @param accumAlphaBits the accum alpha bits
     *                       <p>Default is: 0</p>
     */
    public void setAccumAlphaBits(int accumAlphaBits) {
        if(this.accumAlphaBits == null)
            this.accumAlphaBits = new IntValue();
        this.accumAlphaBits.value = accumAlphaBits;
    }

    /**
     * set the aux buffers
     * @param auxBuffers the aux buffers
     *                   <p>Default is: 0</p>
     */
    public void setAuxBuffers(int auxBuffers) {
        if(this.auxBuffers == null)
            this.auxBuffers = new IntValue();
        this.auxBuffers.value = auxBuffers;
    }

    /**
     * set the samples
     * @param samples the samples
     *                <p>Default is: 0</p>
     */
    public void setSamples(int samples) {
        if(this.samples == null)
            this.samples = new IntValue();
        this.samples.value = samples;
    }

    /**
     * set the refresh rate
     * @param refreshRate the refresh rate
     *                    <p>Default is: {@link GLFW#GLFW_DONT_CARE}</p>
     */
    public void setRefreshRate(int refreshRate) {
        if(this.refreshRate == null)
            this.refreshRate = new IntValue();
        this.refreshRate.value = refreshRate;
    }

    /**
     * set the stereo
     * @param stereo the stereo
     *               <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setStereo(boolean stereo) {
        if(this.stereo == null)
            this.stereo = new IntValue();
        this.stereo.value = stereo ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the srgb capable
     * @param srgbCapable the srgb capable
     *                    <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setSrgbCapable(boolean srgbCapable) {
        if(this.srgbCapable == null)
            this.srgbCapable = new IntValue();
        this.srgbCapable.value = srgbCapable ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the double buffer
     * @param doublebuffer the double buffer
     *                     <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setDoublebuffer(boolean doublebuffer) {
        if(this.doublebuffer == null)
            this.doublebuffer = new IntValue();
        this.doublebuffer.value = doublebuffer ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the client api
     * @param clientApi the client api
     *                  <p>Default is: {@link GLFW#GLFW_OPENGL_API}</p>
     */
    public void setClientApi(int clientApi) {
        if(this.clientApi == null)
            this.clientApi = new IntValue();
        this.clientApi.value = clientApi;
    }

    /**
     * set the context creation api
     * @param contextCreationApi the context creation api
     *                           <p>Default is: {@link GLFW#GLFW_NATIVE_CONTEXT_API}</p>
     */
    public void setContextCreationApi(int contextCreationApi) {
        if(this.contextCreationApi == null)
            this.contextCreationApi = new IntValue();
        this.contextCreationApi.value = contextCreationApi;
    }

    /**
     * set the context version major
     * @param contextVersionMajor the context version major
     *                            <p>Default is: 1</p>
     */
    public void setContextVersionMajor(int contextVersionMajor) {
        if(this.contextVersionMajor == null)
            this.contextVersionMajor = new IntValue();
        this.contextVersionMajor.value = contextVersionMajor;
    }

    /**
     * set the context version minor
     * @param contextVersionMinor the context version minor
     *                            <p>Default is: 0</p>
     */
    public void setContextVersionMinor(int contextVersionMinor) {
        if(this.contextVersionMinor == null)
            this.contextVersionMinor = new IntValue();
        this.contextVersionMinor.value = contextVersionMinor;
    }

    /**
     * set the context robustness
     * @param contextRobustness the context robustness
     *                          <p>Default is: {@link GLFW#GLFW_NO_ROBUSTNESS}</p>
     */
    public void setContextRobustness(int contextRobustness) {
        if(this.contextRobustness == null)
            this.contextRobustness = new IntValue();
        this.contextRobustness.value = contextRobustness;
    }

    /**
     * set the context release behavior
     * @param contextReleaseBehavior the context release behavior
     *                               <p>Default is: {@link GLFW#GLFW_ANY_RELEASE_BEHAVIOR}</p>
     */
    public void setContextReleaseBehavior(int contextReleaseBehavior) {
        if(this.contextReleaseBehavior == null)
            this.contextReleaseBehavior = new IntValue();
        this.contextReleaseBehavior.value = contextReleaseBehavior;
    }

    /**
     * set the context no error
     * @param contextNoError the context no error
     *                       <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setContextNoError(boolean contextNoError) {
        if(this.contextNoError == null)
            this.contextNoError = new IntValue();
        this.contextNoError.value = contextNoError ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the opengl forward compat
     * @param openglForwardCompat the opengl forward compat
     *                            <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setOpenglForwardCompat(boolean openglForwardCompat) {
        if(this.openglForwardCompat == null)
            this.openglForwardCompat = new IntValue();
        this.openglForwardCompat.value = openglForwardCompat ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the opengl debug context
     * @param openglDebugContext the opengl debug context
     *                           <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setOpenglDebugContext(boolean openglDebugContext) {
        if(this.openglDebugContext == null)
            this.openglDebugContext = new IntValue();
        this.openglDebugContext.value = openglDebugContext ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the opengl profile
     * @param openglProfile the opengl profile
     *                      <p>Default is: {@link GLFW#GLFW_OPENGL_ANY_PROFILE}</p>
     */
    public void setOpenglProfile(int openglProfile) {
        if(this.openglProfile == null)
            this.openglProfile = new IntValue();
        this.openglProfile.value = openglProfile;
    }

    /**
     * set the win32 keyboard menu
     * @param win32KeyboardMenu the win32 keyboard menu
     *                          <p>Default is: {@link GLFW#GLFW_FALSE}</p>
     */
    public void setWin32KeyboardMenu(boolean win32KeyboardMenu) {
        if(this.win32KeyboardMenu == null)
            this.win32KeyboardMenu = new IntValue();
        this.win32KeyboardMenu.value = win32KeyboardMenu ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the cocoa retina framebuffer
     * @param cocoaRetinaFramebuffer the cocoa retina framebuffer
     *                               <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setCocoaRetinaFramebuffer(boolean cocoaRetinaFramebuffer) {
        if(this.cocoaRetinaFramebuffer == null)
            this.cocoaRetinaFramebuffer = new IntValue();
        this.cocoaRetinaFramebuffer.value = cocoaRetinaFramebuffer ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the cocoa frame name
     * @param cocoaFrameName the cocoa frame name
     *                       <p>Default is: null</p>
     */
    public void setCocoaFrameName(String cocoaFrameName) {
        this.cocoaFrameName = cocoaFrameName;
    }

    /**
     * set the cocoa graphics switching
     * @param cocoaGraphicsSwitching the cocoa graphics switching
     *                               <p>Default is: {@link GLFW#GLFW_TRUE}</p>
     */
    public void setCocoaGraphicsSwitching(boolean cocoaGraphicsSwitching) {
        if(this.cocoaGraphicsSwitching == null)
            this.cocoaGraphicsSwitching = new IntValue();
        this.cocoaGraphicsSwitching.value = cocoaGraphicsSwitching ? GLFW_TRUE : GLFW_FALSE;
    }

    /**
     * set the x11 class name
     * @param x11ClassName the x11 class name
     *                     <p>Default is: null</p>
     */
    public void setX11ClassName(String x11ClassName) {
        this.x11ClassName = x11ClassName;
    }

    /**
     * set the x11 instance name
     * @param x11InstanceName the x11 instance name
     *                        <p>Default is: null</p>
     */
    public void setX11InstanceName(String x11InstanceName) {
        this.x11InstanceName = x11InstanceName;
    }

    /**
     * create the window in glfw view
     */
    public void createWindow() {
        glfwDefaultWindowHints();
        if(resizable != null)
            glfwWindowHint(GLFW_RESIZABLE, resizable.value);
        if(visible != null)
            glfwWindowHint(GLFW_VISIBLE, visible.value);
        if(decorated != null)
            glfwWindowHint(GLFW_DECORATED, decorated.value);
        if(focused != null)
            glfwWindowHint(GLFW_FOCUSED, focused.value);
        if(autoIconify != null)
            glfwWindowHint(GLFW_AUTO_ICONIFY, autoIconify.value);
        if(floating != null)
            glfwWindowHint(GLFW_FLOATING, floating.value);
        if(maximized != null)
            glfwWindowHint(GLFW_MAXIMIZED, maximized.value);
        if(centerCursor != null)
            glfwWindowHint(GLFW_CENTER_CURSOR, centerCursor.value);
        if(transparentFramebuffer != null)
            glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, transparentFramebuffer.value);
        if(focusOnShow != null)
            glfwWindowHint(GLFW_FOCUS_ON_SHOW, focusOnShow.value);
        if(scaleToMonitor != null)
            glfwWindowHint(GLFW_SCALE_TO_MONITOR, scaleToMonitor.value);
        if(mousePassThrough != null)
            glfwWindowHint(GLFW_MOUSE_PASSTHROUGH, mousePassThrough.value);
        if(redBits != null)
            glfwWindowHint(GLFW_RED_BITS, redBits.value);
        if(greenBits != null)
            glfwWindowHint(GLFW_GREEN_BITS, greenBits.value);
        if(blueBits != null)
            glfwWindowHint(GLFW_BLUE_BITS, blueBits.value);
        if(alphaBits != null)
            glfwWindowHint(GLFW_ALPHA_BITS, alphaBits.value);
        if(depthBits != null)
            glfwWindowHint(GLFW_DEPTH_BITS, depthBits.value);
        if(stencilBits != null)
            glfwWindowHint(GLFW_STENCIL_BITS, stencilBits.value);
        if(accumRedBits != null)
            glfwWindowHint(GLFW_ACCUM_RED_BITS, accumRedBits.value);
        if(accumGreenBits != null)
            glfwWindowHint(GLFW_ACCUM_GREEN_BITS, accumGreenBits.value);
        if(accumBlueBits != null)
            glfwWindowHint(GLFW_ACCUM_BLUE_BITS, accumBlueBits.value);
        if(accumAlphaBits != null)
            glfwWindowHint(GLFW_ACCUM_ALPHA_BITS, accumAlphaBits.value);
        if(auxBuffers != null)
            glfwWindowHint(GLFW_AUX_BUFFERS, auxBuffers.value);
        if(samples != null)
            glfwWindowHint(GLFW_SAMPLES, samples.value);
        if(refreshRate != null)
            glfwWindowHint(GLFW_REFRESH_RATE, refreshRate.value);
        if(stereo != null)
            glfwWindowHint(GLFW_STEREO, stereo.value);
        if(srgbCapable != null)
            glfwWindowHint(GLFW_SRGB_CAPABLE, srgbCapable.value);
        if(doublebuffer != null)
            glfwWindowHint(GLFW_DOUBLEBUFFER, doublebuffer.value);
        if(clientApi != null)
            glfwWindowHint(GLFW_CLIENT_API, clientApi.value);
        if(contextCreationApi != null)
            glfwWindowHint(GLFW_CONTEXT_CREATION_API, contextCreationApi.value);
        if(contextVersionMajor != null)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, contextVersionMajor.value);
        if(contextVersionMinor != null)
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, contextVersionMinor.value);
        if(contextRobustness != null)
            glfwWindowHint(GLFW_CONTEXT_ROBUSTNESS, contextRobustness.value);
        if(contextReleaseBehavior != null)
            glfwWindowHint(GLFW_CONTEXT_RELEASE_BEHAVIOR, contextReleaseBehavior.value);
        if(contextNoError != null)
            glfwWindowHint(GLFW_CONTEXT_NO_ERROR, contextNoError.value);
        if(openglForwardCompat != null)
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, openglForwardCompat.value);
        if(openglDebugContext != null)
            glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, openglDebugContext.value);
        if(openglProfile != null)
            glfwWindowHint(GLFW_OPENGL_PROFILE, openglProfile.value);
        if(win32KeyboardMenu != null)
            glfwWindowHint(GLFW_WIN32_KEYBOARD_MENU, win32KeyboardMenu.value);
        if(cocoaRetinaFramebuffer != null)
            glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, cocoaRetinaFramebuffer.value);
        if(cocoaFrameName != null)
            glfwWindowHintString(GLFW_COCOA_FRAME_NAME, cocoaFrameName);
        if(cocoaGraphicsSwitching != null)
            glfwWindowHint(GLFW_COCOA_GRAPHICS_SWITCHING, cocoaGraphicsSwitching.value);
        if(x11ClassName != null)
            glfwWindowHintString(GLFW_X11_CLASS_NAME, x11ClassName);
        if(x11InstanceName != null)
            glfwWindowHintString(GLFW_X11_INSTANCE_NAME, x11InstanceName);

        window = glfwCreateWindow(width, height, title, 0, 0);
        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            if(window != this.window)
                return;
            this.width = width;
            this.height = height;
        });
        glfwSetWindowPosCallback(window, (window, xpos, ypos) -> {
            if(window != this.window)
                return;
            this.xpos = xpos;
            this.ypos = ypos;
        });
    }

    /**
     * create a window instance with the given name, width, height and title
     * @param width the width of the window
     * @param height the height of the window
     * @param title the title of the window
     */
    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    /**
     * set the window close callback
     * @param callback the callback to set
     */
    public void setWindowCloseCallback(GLFWWindowCloseCallback callback) {
        glfwSetWindowCloseCallback(window, callback);
    }

    /**
     * set the window refresh callback
     * @param callback the callback to set
     */
    public void setWindowRefreshCallback(GLFWWindowRefreshCallback callback) {
        glfwSetWindowRefreshCallback(window, callback);
    }

    /**
     * set the window focus callback
     * @param callback the callback to set
     */
    public void setWindowFocusCallback(GLFWWindowFocusCallback callback) {
        glfwSetWindowFocusCallback(window, callback);
    }

    /**
     * set the window iconify callback
     * @param callback the callback to set
     */
    public void setWindowIconifyCallback(GLFWWindowIconifyCallback callback) {
        glfwSetWindowIconifyCallback(window, callback);
    }

    /**
     * set the window maximize callback
     * @param callback the callback to set
     */
    public void setWindowMaximizeCallback(GLFWWindowMaximizeCallback callback) {
        glfwSetWindowMaximizeCallback(window, callback);
    }

    /**
     * set the framebuffer size callback
     * @param callback the callback to set
     */
    public void setFramebufferSizeCallback(GLFWFramebufferSizeCallback callback) {
        glfwSetFramebufferSizeCallback(window, callback);
    }

    /**
     * set the window content scale callback
     * @param callback the callback to set
     */
    public void setWindowContentScaleCallback(GLFWWindowContentScaleCallback callback) {
        glfwSetWindowContentScaleCallback(window, callback);
    }

    /**
     * set the drop callback
     * @param callback the callback to set
     */
    public void setDropCallback(GLFWDropCallback callback) {
        glfwSetDropCallback(window, callback);
    }

    /**
     * request the window attention
     */
    public void requestWindowAttention() {
        glfwRequestWindowAttention(window);
    }

    /**
     * set the window iconified
     */
    public void iconifyWindow() {
        glfwIconifyWindow(window);
    }

    /**
     * set the window restored
     */
    public void restoreWindow() {
        glfwRestoreWindow(window);
    }

    /**
     * set the window maximized
     */
    public void maximizeWindow() {
        glfwMaximizeWindow(window);
    }

    /**
     * show the window
     */
    public void showWindow() {
        glfwShowWindow(window);
    }

    /**
     * hide the window
     */
    public void hideWindow() {
        glfwHideWindow(window);
    }

    /**
     * focus the window
     */
    public void focusWindow() {
        glfwFocusWindow(window);
    }

    /**
     * set the window position
     * @param xpos the x position of the window
     * @param ypos the y position of the window
     */
    public void setPosition(int xpos, int ypos) {
        this.xpos = xpos;
        this.ypos = ypos;
        glfwSetWindowPos(window, xpos, ypos);
    }

    /**
     * set the window size
     * @param width the width of the window
     * @param height the height of the window
     */
    public void setSize(int width, int height) {
        glfwSetWindowSize(window, width, height);
    }

    /**
     * set the window title
     * @param title the title of the window
     */
    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(window, title);
    }

    /**
     * set the window monitor
     * @param monitor the monitor to set
     * @param xpos the x position of the window
     * @param ypos the y position of the window
     * @param width the width of the window
     * @param height the height of the window
     * @param refreshRate the refresh rate of the window
     */
    public void setMonitor(long monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        glfwSetWindowMonitor(window, monitor, xpos, ypos, width, height, refreshRate);
    }

    /**
     * set the window size limits
     * @param widthMin the minimum width, in screen coordinates, of the content area, or {@link GLFW#GLFW_DONT_CARE} which is -1
     * @param heightMin the minimum height, in screen coordinates, of the content area, or {@link GLFW#GLFW_DONT_CARE} which is -1
     * @param widthMax the maximum width, in screen coordinates, of the content area, or {@link GLFW#GLFW_DONT_CARE} which is -1
     * @param heightMax the maximum height, in screen coordinates, of the content area, or {@link GLFW#GLFW_DONT_CARE} which is -1
     */
    public void setSizeLimit(int widthMin, int heightMin, int widthMax, int heightMax) {
        glfwSetWindowSizeLimits(window, widthMin, heightMin, widthMax, heightMax);
    }

    /**
     * get the id of the window
     * @return the id of the window
     */
    public long id(){
        return window;
    }

    /**
     * get the width of the window
     * @return the width of the window
     */
    public int width(){
        return width;
    }

    /**
     * get the height of the window
     * @return the height of the window
     */
    public int height() {
        return height;
    }

    /**
     * get the title of the window
     * @return the title of the window
     */
    public String title() {
        return title;
    }

    /**
     * get the x position of the window
     * @return the x position of the window
     */
    public int xpos() {
        return xpos;
    }

    /**
     * get the y position of the window
     * @return the y position of the window
     */
    public int ypos() {
        return ypos;
    }

    public void swapBuffers() {
        glfwSwapBuffers(window);
    }

    public void dispose() {
        glfwDestroyWindow(window);
    }
}
