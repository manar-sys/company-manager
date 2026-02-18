import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page, request }) => {

  // Login via API
  const response = await request.post('/auth/login', {
    data: {
      username: 'admin',
      password: '1234'
    }
  });

  const body = await response.json();
  const token = body.token;

  // Inject token into localStorage BEFORE page loads
  await page.addInitScript(token => {
    localStorage.setItem('jwt', token);
  }, token);

  await page.goto('/index.html');
});

test('create → list → update → delete company', async ({ page }) => {

  // CREATE
  await page.fill('#name', 'Playwright Company');
  await page.fill('#budget', '9999');
  await page.click('button[type="submit"]');

  // wait until it appears in DOM
  await expect(page.locator('text=Playwright Company'))
    .toBeVisible({ timeout: 10000 });

  // UPDATE
  await page.locator('text=Edit').last().click();
  await page.fill('#name', 'Playwright Company Updated');
  await page.click('button[type="submit"]');

  await expect(page.locator('text=Playwright Company Updated'))
    .toBeVisible({ timeout: 10000 });

  // DELETE
  await page.locator('text=Delete').last().click();

  await expect(page.locator('text=Playwright Company Updated'))
    .not.toBeVisible({ timeout: 10000 });
});


test('validation error when name is empty', async ({ page }) => {

  // clear name explicitly
  await page.fill('#name', '');
  await page.fill('#budget', '1000');
  await page.click('button[type="submit"]');

  await expect(page.locator('#error')).toBeVisible();
});